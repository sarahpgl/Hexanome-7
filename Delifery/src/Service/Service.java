package Service;

import Donnees.DonneesCarte;
import Donnees.Intersection;
import Donnees.Section;
import Util.FileSystemXML;
import java.util.Map;
import java.util.HashMap;
import Vue.FenetreLancement;
import Vue.MaVue;

import java.util.*;


public class Service {
    public Service() {

    }

    public DonneesCarte creerDonneesCarte(String nomFichier) {
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();


        // Chemin d'accès fixe (à modifier selon vos besoins)
        String cheminFixe = System.getProperty("user.dir") + "/Delifery/fichiersXML2022/";

        // Combinez le chemin fixe et le nom du fichier
        String cheminComplet = cheminFixe + nomFichier;
        Object[] objects = fsxml.lireXML(cheminComplet);

        Intersection[] entrepot = (Intersection[]) objects[0];


        Intersection[] intersections = (Intersection[]) objects[1];
        Section[] sections = (Section[]) objects[2];
        float minLong = (float) objects[3];
        float maxLong = (float) objects[4];
        float minLat = (float) objects[5];
        float maxLat = (float) objects[6];
        float [] origin = {minLat, minLong};
        float longDiff = maxLong - minLong;
        float latDiff = maxLat - minLat;
        float echelleX = 1/latDiff ;
        float echelleY = 1/longDiff ;


        Intersection entrepotDepart = entrepot[0];
        Map<Intersection, Map<Intersection, Float>> carte = new HashMap<>();

        for(Section s : sections) {
            Intersection origine = s.getOrigine();
            Intersection destination = s.getDestination();
            Float taille = s.getTaille();

            // Vérifiez si la clé origine existe dans la carte
            if (!carte.containsKey(origine)) {
                carte.put(origine, new HashMap<>());
            }
            carte.get(origine).put(destination, taille);
        }

        DonneesCarte carteCourante = new DonneesCarte(nomFichier, entrepotDepart, carte,echelleX,echelleY,origin);
        return carteCourante;
    }

    // Méthode qui calcule le plus court chemin entre deux intersections, en utilisant l'algorithme de Dijkstra
    public List<Intersection> dijkstra(Intersection depart, Intersection arrivee, Map<Intersection, Map<Intersection, Float>> graphe) {
        if (!graphe.containsKey(depart) || !graphe.containsKey(arrivee)) {
            System.out.println("Le départ ou l'arrivée n'est pas dans le graphe");
            return null;
        }

        // Création d'une liste pour stocker le chemin final
        List<Intersection> chemin = new ArrayList<>();

        // Création d'un tableau pour stocker les distances minimales entre chaque nœud et le nœud de départ
        Map<Intersection, Float> distances = new HashMap<>();
        // Initialisation des distances à l'infini pour tous les nœuds sauf le nœud de départ
        for (Intersection intersection : graphe.keySet()) {
            distances.put(intersection, Float.MAX_VALUE);
        }
        distances.put(depart, 0f);

        // Création d'un tableau pour stocker les prédécesseurs de chaque nœud dans le chemin optimal
        Map<Intersection, Intersection> predecesseurs = new HashMap<>();

        // Création d'une file de priorité pour stocker les nœuds à visiter, triés par ordre croissant de distance au nœud de départ
        PriorityQueue<Intersection> file = new PriorityQueue<>(Comparator.comparingDouble(intersection -> distances.get(intersection)));

        // Ajout du nœud de départ à la file
        file.add(depart);

        // Tant que la file n'est pas vide et que le nœud d'arrivée n'a pas été atteint
        while (!file.isEmpty() && !file.peek().equals(arrivee)) {
            // Récupération et suppression du nœud le plus proche du nœud de départ dans la file
            Intersection courant = file.poll();
            // Pour chaque voisin du nœud courant
            Map<Intersection, Float> voisins = graphe.get(courant);
            if (voisins != null) {
                for (Map.Entry<Intersection, Float> voisin : voisins.entrySet()) {
                    // Calcul de la distance entre le nœud courant et le voisin
                    float distance = distances.get(courant) + voisin.getValue();
                    // Si la distance est inférieure à la distance minimale actuelle entre le voisin et le nœud de départ
                    Float distanceToVoisin = distances.get(voisin.getKey());
                    if (distanceToVoisin == null || distance < distanceToVoisin) {
                        // Mise à jour de la distance minimale pour le voisin
                        distances.put(voisin.getKey(), distance);
                        // Mise à jour du prédécesseur du voisin
                        predecesseurs.put(voisin.getKey(), courant);
                        // Ajout du voisin à la file
                        file.add(voisin.getKey());
                    }
                }
            }
        }

        // Si le nœud d'arrivée a été atteint
        if (distances.containsKey(arrivee) && distances.get(arrivee) != Float.MAX_VALUE) {
            // Reconstruction du chemin en partant du nœud d'arrivée et en remontant les prédécesseurs
            Intersection intersection = arrivee;
            while (intersection != null) {
                chemin.add(0, intersection);
                intersection = predecesseurs.get(intersection);
            }
            System.out.println("Distance du chemin : "+distances.get(arrivee));
            return chemin;
        } else {
            // Sinon, retour de null pour indiquer qu'aucun chemin n'existe
            return null;
        }
    }
}
