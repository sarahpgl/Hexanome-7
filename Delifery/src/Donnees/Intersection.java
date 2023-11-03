package Donnees;

import java.math.BigInteger;
import java.util.Objects;
import Util.Coordonnees;

public class Intersection {
    private BigInteger id;
    private Coordonnees coordonnees;

    public Intersection(BigInteger id, Coordonnees coordonnees) {
        this.id = id;
        this.coordonnees = coordonnees;
    }

    public Intersection(Intersection intersection) {
        this.id = intersection.id;
        this.coordonnees = intersection.coordonnees;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Coordonnees getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(Coordonnees coordonnees) {
        this.coordonnees = coordonnees;
    }

    public String toString (){
        return "id :" + id + " ; Coordonnés :" + coordonnees.toString();
    }

    public double getDistanceTo (Intersection intersection){
        return this.getCoordonnees().getDistanceTo(intersection.getCoordonnees());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
