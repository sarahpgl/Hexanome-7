package Donnees;

import java.util.List;

public class CatalogueTours {

    public List<Tour> catalogue;

    public Tour getTourById(Long id) {
        for(Tour tour: catalogue) {
            if(tour.getId() == id) return tour;
        }
        return null;
    }
}
