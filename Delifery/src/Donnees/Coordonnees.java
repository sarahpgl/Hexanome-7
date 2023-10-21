package Donnees;

public class Coordonnees {

    private Integer latitude;
    private Integer longitude;

    public Coordonnees(Integer latitude, Integer longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public String ToString () {
        return "Longitude : " + this.getLongitude() + " ; Latitude " + getLongitude();
    }
}
