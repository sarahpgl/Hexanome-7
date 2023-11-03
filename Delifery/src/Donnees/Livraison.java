package Donnees;

import Util.Coordonnees;

import java.time.LocalTime;

public class Livraison {

    private Long id;

    private Intersection adresse;

    private Creneau creneau;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    public Livraison(Long id, Intersection adresse, Creneau creneau, LocalTime heureDebut, LocalTime heureFin) {
        this.id = id;
        this.adresse = adresse;
        this.creneau = creneau;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public Livraison(Long id, Intersection adresse, Creneau creneau) {
        this.id = id;
        this.adresse = adresse;
        this.creneau = creneau;
    }

    public Livraison(Long id, Intersection adresse) {
        this.id = id;
        this.adresse = adresse;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Intersection getAdresse() {
        return adresse;
    }

    public void setAdresse(Intersection adresse) {
        this.adresse = adresse;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public double getDistanceTo(Livraison livraison){
        return this.getAdresse().getDistanceTo(livraison.getAdresse());
    }

    public double getDistanceTo(Coordonnees coordonnees){
        return this.getAdresse().getCoordonnees().getDistanceTo(coordonnees);
    }

}
