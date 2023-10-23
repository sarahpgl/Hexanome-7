package Service;
import Donnees.DonneesCarte;
import Donnees.Intersection;
import Donnees.Section;
import Util.FileSystemXML;
import Vue.FenetreLancement;
import Vue.MaVue;

import java.util.*;

public class Service {
    public Service(){

    }
    public DonneesCarte creerDonneesCarte(String nomFichier) {
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();

        // Chemin d'accès fixe (à modifier selon vos besoins)
        String cheminFixe = System.getProperty("user.dir")+"/fichiersXML2022/";

        // Combinez le chemin fixe et le nom du fichier
        String cheminComplet = cheminFixe + nomFichier;
        Object[] objects = fsxml.lireXML(cheminComplet);

        Intersection[] entrepot = (Intersection[]) objects[0];
        Intersection[] intersections = (Intersection[]) objects[1];
        Section[] sections = (Section[]) objects[2];

        Intersection entrepotDepart = entrepot[0];
        Map<Intersection, Map<Intersection, Float>> carte = new HashMap<>();

        for(Section s : sections){
            Intersection origine = s.getOrigine();
            Intersection destination = s.getDestination();
            Float taille = s.getTaille();

            // Vérifiez si la clé origine existe dans la carte
            if (!carte.containsKey(origine)) {
                carte.put(origine, new HashMap<>());
            }
            carte.get(origine).put(destination, taille);
        }

        DonneesCarte carteCourante = new DonneesCarte(nomFichier, entrepotDepart, carte);
        return carteCourante;
    }

    // Méthode qui calcule le plus court chemin entre deux intersections, en utilisant l'algorithme de Dijkstra
    public List<Intersection> dijkstra(Intersection depart, Intersection arrivee, Map<Intersection, Map<Intersection, Integer>> graphe) {
        // Création d'une liste pour stocker le chemin final
        List<Intersection> chemin = new ArrayList<>();

        // Création d'une map pour stocker les distances minimales entre chaque noeud et le noeud de départ
        Map<Intersection, Integer> distances = new HashMap<>();

        // Création d'une map pour stocker les prédécesseurs de chaque noeud dans le chemin optimal
        Map<Intersection, Intersection> predecesseurs = new HashMap<>();

        // Création d'une file de priorité pour stocker les noeuds à visiter, en les triant par ordre croissant de distance au noeud de départ
        PriorityQueue<Intersection> file = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialisation des distances à l'infini pour tous les noeuds sauf le noeud de départ
        for (Intersection intersection : graphe.keySet()) {
            distances.put(intersection, Integer.MAX_VALUE);
        }
        distances.put(depart, 0);
        // Ajout du noeud de départ à la file
        file.add(depart);
        // Tant que la file n'est pas vide et que le noeud d'arrivée n'a pas été atteint
        while (!file.isEmpty() && !file.peek().equals(arrivee)) {
            // Récupération et suppression du noeud le plus proche du noeud de départ dans la file
            Intersection courant = file.poll();
            // Pour chaque voisin du noeud courant
            for (Intersection voisin : graphe.get(courant).keySet()) {
                // Calcul de la distance entre le noeud courant et le voisin
                int distance = distances.get(courant) + graphe.get(courant).get(voisin);
                // Si la distance est inférieure à la distance minimale actuelle entre le voisin et le noeud de départ
                if (distance < distances.get(voisin)) {
                    // Mise à jour de la distance minimale pour le voisin
                    distances.put(voisin, distance);
                    // Mise à jour du prédécesseur du voisin
                    predecesseurs.put(voisin, courant);
                    // Ajout du voisin à la file
                    file.add(voisin);
                }
            }
        }
        // Si le noeud d'arrivée a été atteint
        if (distances.get(arrivee) != Integer.MAX_VALUE) {
            // Reconstruction du chemin en partant du noeud d'arrivée et en remontant les prédécesseurs
            Intersection intersection = arrivee;
            while (intersection != null) {
                chemin.add(0, intersection);
                intersection = predecesseurs.get(intersection);
            }
            // Retour du chemin
            return chemin;
        } else {
            // Sinon, retour de null pour indiquer qu'aucun chemin n'existe
            return null;
        }
    }
}
