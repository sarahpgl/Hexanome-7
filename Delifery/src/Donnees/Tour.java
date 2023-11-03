package Donnees;

import java.util.ArrayList;

public class Tour {

    private Long id;

    private ArrayList<Livraison> livraisons;

    private ArrayList<Intersection> trajet;

    public Tour(Long id, ArrayList<Livraison> livraisons, ArrayList<Intersection> trajet) {
        this.id = id;
        this.livraisons = livraisons;
        this.trajet = trajet;
    }

    public Tour(Long id, ArrayList<Livraison> livraisons) {
        this.id = id;
        this.livraisons = livraisons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(ArrayList<Livraison> livraisons) {
        this.livraisons = livraisons;
    }

    public ArrayList<Intersection> getTrajet() {
        return trajet;
    }

    public void setTrajet(ArrayList<Intersection> trajet) {
        this.trajet = trajet;
    }

}


