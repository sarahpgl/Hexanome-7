package Donnees;

public class Intersection {
    private Integer id;
    private Coordonnees coordonnees;

    public Intersection(Integer id, Coordonnees coordonnees) {
        this.id = id;
        this.coordonnees = coordonnees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Coordonnees getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(Coordonnees coordonnees) {
        this.coordonnees = coordonnees;
    }
}
