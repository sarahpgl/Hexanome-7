package Donnees;

public class Livraison {

    private Long id;

    private Intersection adresse;

    private Creneau creneau;

    public Livraison(Long id, Intersection adresse, Creneau creneau) {
        this.id = id;
        this.adresse = adresse;
        this.creneau = creneau;
    }
}
