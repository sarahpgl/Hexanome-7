package Donnees;

import java.util.ArrayList;
import java.util.List;

public class CatalogueTours {

    public ArrayList<Tour> catalogue;

    public Tour getTourById(Long id) {
        for(Tour tour: catalogue) {
            if(tour.getId() == id) return tour;
        }
        return null;
    }

    public ArrayList<Tour> getCatalogue() {
        return catalogue;
    }
}
