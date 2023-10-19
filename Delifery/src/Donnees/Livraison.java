package Donnees;

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
}
