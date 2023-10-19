package Donnees;

public enum Creneau {
    HUIT_NEUF(8),
    NEUF_DIX(9),
    DIX_ONZE(10),
    ONZE_DOUZE(11);

    private final int valeur;

    private Creneau(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur() {
        return valeur;
    }
}

