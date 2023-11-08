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

    private float[] origine;

    public DonneesCarte(String idDonneesCarte, Intersection entrepot, Map<Intersection, Map<Intersection, Float>> carte, float x, float y, float [] origin) {
        this.idDonneesCarte = idDonneesCarte;
        this.entrepot = entrepot;
        this.carte = carte;
        this.echelleX = x;
        this.echelleY = y;
        this.origine = origin;
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

    public float[] getOrigine() {
        return origine;
    }

    public void setOrigine(float[] origine) {
        this.origine = origine;
    }

    public Intersection findIntersectionById(String id) {
        BigInteger bigId = new BigInteger(id);
        // On parcourt la première clé de la map
        for (Intersection inter : carte.keySet()) {
            // On vérifie si l'id de l'intersection correspond au paramètre
            if (inter.getId().equals(bigId)) {
                // On renvoie l'intersection trouvée
                return inter;
            }
        }
        // Si aucune intersection n'a été trouvée, on renvoie null
        return null;
    }
}