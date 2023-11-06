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

    private String nom;
    private static Integer VITESSE = 15;

    public Livreur(Integer tourNumber) {
        this.nom = names[tourNumber + 1];
    }

    public String getNom(){
        return this.nom;
    }


}
