package Donnees;

public enum Creneau {
    CRENEAU_8H_9H("8h-9h", 1),
    CRENEAU_9H_10H("9h-10h", 2),
    CRENEAU_10H_11H("10h-11h", 3),
    CRENEAU_11H_12H("11h-12h", 4);

    private final String plageHoraire;
    private final int valeur;

    private Creneau(String plageHoraire, int valeur) {
        this.plageHoraire = plageHoraire;
        this.valeur = valeur;
    }
}
