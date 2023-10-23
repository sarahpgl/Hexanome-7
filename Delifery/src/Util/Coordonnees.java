package Util;

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
}
