package Donnees;

import Util.Calculs;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Tour {

    private Long id;

    private ArrayList<Livraison> livraisons;
    private Livreur livreur;
    private ArrayList<Intersection> trajet;

    public Tour(Long id, ArrayList<Livraison> livraisons, ArrayList<Intersection> trajet) {
        this.id = id;
        this.livreur = new Livreur(this.id.intValue());
        this.livraisons = livraisons;
        this.trajet = trajet;
    }

    public Tour(Long id, ArrayList<Livraison> livraisons) {
        this.id = id;
        this.livreur = new Livreur(this.id.intValue());
        this.livraisons = livraisons;
        trajet=new ArrayList<Intersection>();
    }

    public Tour(Long id) {
        this.id = id;
        this.livreur = new Livreur(this.id.intValue());
        this.livraisons = new ArrayList<Livraison>();
        trajet=new ArrayList<Intersection>();
    }

    public Long getId() {
        return id;
    }

    public String getNomLivreur() { return livreur.getNom(); }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(ArrayList<Livraison> livraisons) {
        this.livraisons = livraisons;
    }
    public void addLivraisonsTour(Livraison livraison) {
        livraisons.add(livraison);
    }
    public ArrayList<Intersection> getTrajet() {
        return trajet;
    }

    public void setTrajet(ArrayList<Intersection> trajet) {
        this.trajet = trajet;
    }

    //Méthode qui concaténe au trajet une liste d'intersection. On vérifie à ne pas introduire de doublon à la suite avant d'ajouter le sous trajet au trajet
    public void ajouterListeAuTrajet(List<Intersection> liste) {
        if (!liste.isEmpty()) {
            Intersection dernier = trajet.get(trajet.size() - 1);
            Intersection premier = liste.get(0);
            if (dernier.equals(premier)) {
                // Si les deux éléments sont égaux, on supprime le premier de la liste à ajouter
                liste.remove(0);
            }
            trajet.addAll(liste);
        }
    }

    public void ajouterIntersection(Intersection intersection){
        trajet.add(intersection);
    }

    public String toString(){
        String mes= "Tour numéro "+this.id+"\n";
        mes= mes +"Livraisons effectuées :\n";
        Calculs.trierLivraisons(livraisons);
        for (Livraison l: livraisons) {
            if(l.livree()==true){
                mes = mes + l.toString() +"\n";
            }
        }
        mes = mes + "\n";
        mes = mes + "Trajet parcouru : \n";

        int i=0;
        for (Intersection intersection : trajet) {
            if(i==0){
                mes = mes + "Intersection "+i+" : " + intersection.getId();
            }else {
                mes = mes + " | Intersection "+i+" : " + intersection.getId();
            }
            i++;
        }

        mes= mes +"\n"+"Livraisons non effectuées :\n";
        for (Livraison l: livraisons) {
            if(l.livree()!=true){
                mes = mes + l.toString() +"\n";
            }
        }
        return mes;
    }

    public void reinitialiserTrajet(){
        trajet.clear();
    }

    public Livreur getLivreur() {
        return livreur;
    }

    public boolean toutesLivraisonsAcceptees() {

        for (Livraison l : this.livraisons) {
            if ((l.getHeureDepart() == LocalTime.of(0, 0, 0)) && (l.getHeureArrivee() == LocalTime.of(0, 0, 0))) {
                return false;

            }

        }
        return true;
    }
}


