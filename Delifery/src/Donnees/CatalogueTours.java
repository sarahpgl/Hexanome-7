package Donnees;

import java.util.ArrayList;
import java.util.List;

public class CatalogueTours {

    public List<Tour> catalogue;

    public CatalogueTours(){
        catalogue= new ArrayList<>();
    }

    public Tour getTourById(Long id) {
        for(Tour tour: catalogue) {
            if(tour.getId() == id) return tour;
        }
        return null;
    }

    public List<Tour> getCatalogue(){
        return catalogue;
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

    public Tour getTourById()
}
