package Donnees;
import Donnees.Intersection;
import java.math.BigInteger;
import java.util.Map;

public class DonneesCarte {
    private String idDonneesCarte;
    private Intersection entrepot;
    private Map<Intersection, Map <Intersection, Float>> carte;
    private Integer echelleX;
    private Integer echelleY;

    public DonneesCarte(String idDonneesCarte, Intersection entrepot, Map<Intersection, Map<Intersection, Float>> carte) {
        this.idDonneesCarte = idDonneesCarte;
        this.entrepot = entrepot;
        this.carte = carte;
        this.echelleX = 1;
        this.echelleY = 1;
    }

    public Map<Intersection, Map <Intersection, Float>> getCarte(){
        return  carte;
    }
}
