package Util;

import Donnees.Intersection;

import java.util.*;

public class Calculs {


    // Méthode qui calcule le plus court chemin entre deux intersections, en utilisant l'algorithme de Dijkstra
    public static List<Intersection> dijkstra(Intersection depart, Intersection arrivee, Map<Intersection, Map<Intersection, Float>> graphe) {
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

    //Cette méthode renvoie la distance nécessaire pour parcourir la liste d'intersection rentrée en paramètre
    public static double getDistanceChemin (List<Intersection> chemin){
        double distance=0;
        if(chemin!=null){
            Intersection interCourante = chemin.get(0);
            for(Intersection inter: chemin) {
                if (!inter.equals(interCourante)){
                    distance = distance + inter.getCoordonnees().getDistanceTo(interCourante.getCoordonnees());
                }
                interCourante=inter;
            }
        }
        return distance;
    }
}
