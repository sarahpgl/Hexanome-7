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
    private static double VITESSE = 15;
    private Long id;

    private String nom;

    public Livreur(String name) {
        this.nom = name;
        this.id = getIdByName(name);
    }

    public Livreur(String name){
        this.nom = name;
        this.id = getIdByName(name);
    }
    public Livreur(Integer tourNumber) {
        this.nom = names[tourNumber - 1];
    }

    public String getNom() {
        return this.nom;
    }

    public double getVitesse() {
        return VITESSE;
    }


    public Long getIdByName(String name) {
        // Parcourir le tableau à la recherche de la valeur
        for (int i = 0; i < names.length; i++) {
            // Comparer la valeur avec l’élément du tableau à l’indice i
            if (names[i].equals(name)) {
                return (long) i;
            }
        }
        return -1L;

    }

    public Long getId() {
        return this.id;
    }
}
