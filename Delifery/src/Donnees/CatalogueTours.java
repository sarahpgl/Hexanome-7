package Donnees;

import java.util.ArrayList;
import java.util.List;

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
}
