package Donnees;

import Util.Coordonnees;

import java.time.LocalTime;

public class Livraison {

    private Long id;

    private Intersection adresse;

    private Creneau creneau;
    private LocalTime heureArrivee;
    private LocalTime heureDepart;

    public Livraison(Long id, Intersection adresse, Creneau creneau, LocalTime heureDebut, LocalTime heureFin) {
        this.id = id;
        this.adresse = adresse;
        this.creneau = creneau;
        this.heureArrivee = heureDebut;
        this.heureDepart = heureFin;
    }

    public Livraison(Long id, Intersection adresse) {
        this.id = id;
        this.adresse = adresse;
        this.creneau = Creneau.valueOf("HUIT_NEUF"); // valeur par défaut
        livraisonNonLivree();
    }

    public Livraison(Long id, Intersection adresse,Creneau creneau) {
        this.id = id;
        this.adresse = adresse;
        this.creneau = creneau;
        livraisonNonLivree();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Intersection getAdresse() {
        return adresse;
    }

    public void setAdresse(Intersection adresse) {
        this.adresse = adresse;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public LocalTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public double getDistanceTo(Livraison livraison){
        return this.getAdresse().getDistanceTo(livraison.getAdresse());
    }

    public double getDistanceTo(Coordonnees coordonnees){
        return this.getAdresse().getCoordonnees().getDistanceTo(coordonnees);
    }

    //Méthode qui intancie l'heure en fonction du créneau de la livraison
    public LocalTime mettreAJourHeure (Creneau creaneau, LocalTime heure){
        heure = LocalTime.of(creneau.getValeur(), 0, 0);
        return heure;
    }

    //Méthode qui met à jour l'heure de départ (heureArrivee + 5 minutes)
    public void mettreAJourHeureDepart(LocalTime heureArrivee){
        this.heureDepart=heureArrivee.plusMinutes(5);
    }

    public void livraisonNonLivree(){
        this.heureArrivee= LocalTime.of(0,0,0);
        this.heureDepart= LocalTime.of(0,0,0);
    }

    //Méthode qui sert à mettre à jour l'heure d'arrivée et de départ en fontion de l'heure de la livraison précédante, de la distance parcouru et de la vitesse du livreur
    public void mettreAJourHeure(LocalTime heure, double distance, double vitesse) {
        this.heureArrivee=heure;
        // On convertit la vitesse en m/s
        vitesse = vitesse * 1000 / 3600;
        // On calcule le temps nécessaire pour parcourir la distance en secondes
        double temps = distance / vitesse;
        // On ajoute le temps à l'heure de départ en tenant compte des minutes et des secondes
        this.heureArrivee = this.heureArrivee.plusHours((int) temps / 3600); // On ajoute les heures
        this.heureArrivee = this.heureArrivee.plusMinutes(((int) temps % 3600) / 60); // On ajoute les minutes
        this.heureArrivee = this.heureArrivee.plusSeconds(((int) temps % 3600) % 60); // On ajoute les secondes
        //Si le livreur arrive à la livraison avant l'horaire du créneau alors la livraison commencera au créneau de cette dernière
        if(this.heureArrivee.getHour()<this.creneau.getValeur()){
            this.heureArrivee =mettreAJourHeure(this.creneau,this.heureArrivee);
        }
        //On met à jour l'heure de départ du point de livraison
        mettreAJourHeureDepart(heureArrivee);
    }

    public String toString(){
        return "id : "+this.id + "; Adresse : "+ this.adresse.toString() + "; HeureArrivée : "+this.getHeureArrivee()+"; HeureDepart : "+this.getHeureDepart()+"; Creneau : "+this.creneau.getValeur();
    }

    public boolean livree(){
        boolean b= this.heureArrivee != LocalTime.of(0, 0, 0);
        return b;
    }

}
