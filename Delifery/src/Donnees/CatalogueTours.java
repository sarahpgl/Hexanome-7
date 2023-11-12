package Donnees;

import java.util.ArrayList;
import Donnees.Livreur;

public class CatalogueTours {

    public ArrayList<Tour> catalogue;
    public String mapName;
    public Integer nbLivreurs;

    public CatalogueTours(){
        catalogue= new ArrayList<Tour>();
        nbLivreurs = 0;
    }

    public CatalogueTours(String mapName) {
        this.mapName = mapName;
        catalogue = new ArrayList<Tour>();
        nbLivreurs = 0;
        }

    public Tour getTourById(Long id) {
        for(Tour tour: catalogue) {
            if(tour.getId() == id) return tour;
        }
        return null;
    }

    public void ajouterTour(Tour tour){
        catalogue.add(tour);
    }
    public void supprimerTour(){
        catalogue.remove(catalogue.getLast());
    }

    public Tour getTourByLivreur (Livreur l){
        for (Tour t: catalogue) {
            if (t.getLivreur().getId()==l.getId()){
                return t;
            }
        }
        return null;
    }

    public Livreur getLivreur(int index){

       for (Tour t: catalogue){
           if (t.getId() == index){
               return t.getLivreur();
           }


       }
       return null;

    }

    public ArrayList<Tour> getCatalogue() {
        return catalogue;
    }
    public void setNbLivreurs(Integer nb){

        this.nbLivreurs = nb ;

    }
    public Integer getNbTour(){
        return catalogue.size();
    }
    public ArrayList<Livreur> getListeLivreurs(){
        ArrayList<Livreur> liste = new ArrayList<Livreur>();
        for(Tour t : catalogue){
            liste.add(t.getLivreur());
        }
        return liste;
    }

    public String getMapName() {
        return mapName;
    }

    public String toString(){
        String mes ="************* Catalogue Tout *************\n";
        if (this.mapName != null){
            mes = mes + "Map :" + this.getMapName() +"\n";
        }
        for (Tour t: this.catalogue) {
            mes = mes + t.toString();
        }
        return  mes;
    }

}
