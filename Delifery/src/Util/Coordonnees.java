package Util;
import java.lang.Math;

public class Coordonnees {

    private Double latitude;
    private Double longitude;

    public Coordonnees(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String toString () {
        return "(Longitude : " + this.getLongitude() + " ; Latitude " + getLatitude() +")";
    }

    // Définir la méthode getDistanceTo qui prend en paramètre un objet Coordonnees
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
