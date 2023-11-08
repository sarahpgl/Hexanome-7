package Donnees;

public class Livreur {

    static String names[] = {
            "Moïse",
            "Adam Labrosse",
            "Fatih",
            "Sophie",
            "Pierric",
            "Jean Yves Duplantis",
            "Judith",
            "Nathalie",
            "Abdel Karim",
            "Maria",
            "Virginie",
            "Branda"
    };

    private Long id;
    private String nom;
    private static double VITESSE = 15;

    public Livreur(Integer tourNumber) {
        this.nom = names[tourNumber - 1];
    }

    public String getNom(){
        return this.nom;
    }

    public double getVitesse() {
        return VITESSE;
    }

    public Long getId(){
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(this.nom)) {
                return (long)i;
            }
        }
        return (long)-1;
    }
}
