package Donnees;
import Donnees.Intersection;
import java.math.BigInteger;
import java.util.Map;

public class DonneesCarte {
    private String idDonneesCarte;
    private Intersection entrepot;
    private Map<Intersection, Map <Intersection, Float>> carte;
    private float echelleX;
    private float echelleY;

    public DonneesCarte(String idDonneesCarte, Intersection entrepot, Map<Intersection, Map<Intersection, Float>> carte, float x, float y) {
        this.idDonneesCarte = idDonneesCarte;
        this.entrepot = entrepot;
        this.carte = carte;
        this.echelleX = x;
        this.echelleY = y;
    }

    public float getEchelleX() {
        return echelleX;
    }

    public void setEchelleX(float echelleX) {
        this.echelleX = echelleX;
    }

    public float getEchelleY() {
        return echelleY;
    }

    public void setEchelleY(float echelleY) {
        this.echelleY = echelleY;
    }

    public Map<Intersection, Map<Intersection, Float>> getCarte() {
        return carte;
    }

    public void setCarte(Map<Intersection, Map<Intersection, Float>> carte) {
        this.carte = carte;
    }

    public Intersection getEntrepot() {
        return entrepot;
    }

    public void setEntrepot(Intersection entrepot) {
        this.entrepot = entrepot;
    }
}