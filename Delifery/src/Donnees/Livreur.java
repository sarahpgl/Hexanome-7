package Donnees;

public class Livreur {

    private Integer id;
    private static Integer VITESSE = 15;

    public Livreur(Integer id) {
        this.id = id;
    }

    public Integer getId(){
        return this.id;
    }

    public boolean equals(Livreur l){
        return this.id.equals(l.getId());
    }
}
