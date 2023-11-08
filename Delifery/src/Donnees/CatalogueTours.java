package Donnees;

import java.util.ArrayList;
import Donnees.Livreur;

public class CatalogueTours {

    public ArrayList<Tour> catalogue;
    public String mapName;

    public CatalogueTours(){
        catalogue= new ArrayList<Tour>();
    }

    public CatalogueTours(String mapName) {
        this.mapName = mapName;
        catalogue = new ArrayList<Tour>();
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

    public String toString(){
        String mes = "Catalogue Tour : \n";
        for (Tour t: catalogue) {
            mes = mes + t.toString() +"\n";
        }
        return mes;
    }

    public Tour getTourByLivreur (Livreur l){
        for (Tour t: catalogue) {
            if (t.getLivreur().getId()==l.getId()){
                return t;
            }
        }
        return null;
    }

    public ArrayList<Tour> getCatalogue() {
        return catalogue;
    }
}
