package Donnees;

public class Section {
    private Float taille;
    private String nom;
    private Intersection origine;
    private Intersection destination;

    public Section(Float taille, String nom, Intersection origine, Intersection destination) {
        this.taille = taille;
        this.nom = nom;
        this.origine = origine;
        this.destination = destination;
    }

    public Float getTaille() {
        return taille;
    }

    public void setTaille(Float taille) {
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

    public String toString (){
        return "Nom : " + nom + " ; Longueur : " +taille+ " ; Origine: ["+origine.toString()+"] ; Destination : ["+destination.toString()+"]";
    }
}
