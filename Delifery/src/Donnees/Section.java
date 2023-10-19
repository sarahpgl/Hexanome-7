package Donnees;

public class Section {
    private Integer taille;
    private String nom;
    private Intersection origine;
    private Intersection destination;

    public Section(Integer taille, String nom, Intersection origine, Intersection destination) {
        this.taille = taille;
        this.nom = nom;
        this.origine = origine;
        this.destination = destination;
    }

    public Integer getTaille() {
        return taille;
    }

    public void setTaille(Integer taille) {
        this.taille = taille;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Intersection getOrigine() {
        return origine;
    }

    public void setOrigine(Intersection origine) {
        this.origine = origine;
    }

    public Intersection getDestination() {
        return destination;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }
}
