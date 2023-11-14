package Util;
import java.lang.Math;

public class Coordonnees {

    private Double latitude;
    private Double longitude;

    /**
     * Initialise les coordonnées avec la latitude et la longitude spécifiées.
     *
     * @param latitude  La latitude des coordonnées.
     * @param longitude La longitude des coordonnées.
     */
    public Coordonnees(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Renvoie la latitude des coordonnées.
     *
     * @return La latitude des coordonnées.
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Modifie la latitude des coordonnées.
     *
     * @param latitude La nouvelle valeur de latitude.
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Renvoie la longitude des coordonnées.
     *
     * @return La longitude des coordonnées.
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Modifie la longitude des coordonnées.
     *
     * @param longitude La nouvelle valeur de longitude.
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Renvoie une représentation en chaîne des coordonnées.
     *
     * @return Une chaîne représentant les coordonnées.
     */
    public String toString () {
        return "(Longitude : " + this.getLongitude() + " ; Latitude " + getLatitude() +")";
    }

    // Définir la méthode getDistanceTo qui prend en paramètre un objet Coordonnees
    /**
     * Calcule la distance en mètres entre ces coordonnées et une autre paire de coordonnées.
     *
     * @param autre Les coordonnées pour lesquelles on calcule la distance.
     * @return La distance en mètres entre ces coordonnées et 'autre'.
     */
    public double getDistanceTo(Coordonnees autre) {
        // Convertir les degrés en radians
        double lon1 = Math.toRadians(this.longitude);
        double lat1 = Math.toRadians(this.latitude);
        double lon2 = Math.toRadians(autre.longitude);
        double lat2 = Math.toRadians(autre.latitude);

        // Calculer la distance angulaire entre les deux points
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        // Définir le rayon de la terre en mètres
        double r = 6371000;

        // Calculer la distance en mètres et la renvoie
        return ((double) r * c);
    }
}
