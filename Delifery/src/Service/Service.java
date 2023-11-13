package Service;

import Donnees.*;
import Vue.*;
import Util.Calculs;
import Util.Coordonnees;
import Util.FileSystemXML;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.Map;
import java.util.HashMap;

import Vue.FenetreLancement;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.time.LocalTime;

import java.util.*;

import static javafx.application.Application.launch;


public class Service {

    private static volatile Service instance;
    private DonneesCarte donneesCarte;
    private CatalogueTours catalogueTours;

    private VueApplication vueApplication;

    private Carte carte;

    private String nomFichierCarte;


    public Service() {
        this.catalogueTours = new CatalogueTours();
    }

    public static Service getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new Service();
                }
            }
        }
        return instance;
    }

    public DonneesCarte creerDonneesCarte(String nomFichier) {
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();

        String nomFichierCarte;
        // Chemin d'accès fixe (à modifier selon vos besoins)
        String cheminFixe = System.getProperty("user.dir") + "/Delifery/fichiersXML2022/";

        String cheminComplet = cheminFixe + nomFichier;

        //System.out.println("Chemin Fixe (Methode creerDonneesCarte dans Service) : "+cheminComplet);

        Object[] objects = fsxml.lireXML(nomFichier);

        if (objects!=null && objects[0] instanceof CatalogueTours) {
            this.catalogueTours = (CatalogueTours)objects[0];
            System.out.println("Print de la méthode creerDonnerCartes de la class Service qui print le catalgue Tour dans le cas où le fichier xml correspond à la restitution d'un catalogueTout : \n" + this.getCatalogueTours().toString());
            nomFichierCarte = this.catalogueTours.getMapName();
            cheminComplet = cheminFixe + nomFichierCarte;
            this.nomFichierCarte=nomFichierCarte;
            objects = fsxml.lireXML(nomFichierCarte);
        } else {
            this.nomFichierCarte = nomFichier;
        }

        if(objects==null) {
            objects = fsxml.lireXML(cheminComplet);
        }

        Intersection[] entrepot = (Intersection[]) objects[0];


        Intersection[] intersections = (Intersection[]) objects[1];
        Section[] sections = (Section[]) objects[2];
        float minLong = (float) objects[3];
        float maxLong = (float) objects[4];
        float minLat = (float) objects[5];
        float maxLat = (float) objects[6];
        float [] origin = {minLat, minLong};
        float longDiff = maxLong - minLong;
        float latDiff = maxLat - minLat;
        float echelleX = 1/latDiff ;
        float echelleY = 1/longDiff ;


        Intersection entrepotDepart = entrepot[0];
        Map<Intersection, Map<Intersection, Float>> carte = new HashMap<>();

        for(Section s : sections) {
            Intersection origine = s.getOrigine();
            Intersection destination = s.getDestination();
            Float taille = s.getTaille();

            // Vérifiez si la clé origine existe dans la carte
            if (!carte.containsKey(origine)) {
                carte.put(origine, new HashMap<>());
            }
            carte.get(origine).put(destination, taille);
        }

        DonneesCarte carteCourante = new DonneesCarte(nomFichier, entrepotDepart, carte,echelleX,echelleY,origin);
        this.donneesCarte = carteCourante;
        return carteCourante;
    }

    public Tour creerTour(ArrayList<Livraison> livraisons, ArrayList<Intersection> intersections, Livreur livreur){

        Tour tour = new Tour( livraisons, intersections, livreur);

        return tour;
    }
    public Boolean essaieAjoutLivraisonAuTour(Intersection adresse, Creneau creneau, Livreur livreur){
        Livraison l = new Livraison((long)(Math.random()*1000),adresse,  creneau);
        // chercher le tour auquel correspond le livreur
        CatalogueTours catalogueTours = this.catalogueTours;
        Tour tour = catalogueTours.getTourByLivreur(livreur);
        if (tour == null){
            ArrayList<Livraison> livraisons = new ArrayList<Livraison>();
            livraisons.add(l);
             tour = new Tour(livraisons, livreur);
             catalogueTours.ajouterTour(tour);
        }else {
            tour.addLivraisonsTour((l));
        }
        DonneesCarte dc = this.donneesCarte;

        tour = calculerTour( tour, 15.0, dc,  dc.getEntrepot());
        if (!tour.toutesLivraisonsAcceptees()){
            return false ;
        }
        // essayer de calculer le tour (modifier dans la fonction qui calcule le tour pour renvoyer l'état de l'opération)
        // si ok
        updateCarte();
        updatePanel();
        return true;
    }
    public Livraison creerLivraison(Long id, Intersection adresse, Creneau creneau, LocalTime heureDebut, LocalTime heureFin){
        Livraison l = new Livraison(id,adresse,  creneau,  heureDebut,  heureFin);
        return l;
    }
    public Intersection creerIntersection(BigInteger id, Coordonnees coordonnees){
        Intersection i = new Intersection(id, coordonnees);
        return i ;
    }
    public ArrayList<Intersection> creerIntersectionsPourLivrer(){
        Intersection destination3 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214,4.875591));
        Intersection destination4 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969,4.873468));
        Intersection destination5 = new Intersection(new BigInteger("25321422"), new Coordonnees(45.749027,4.873145));
        Intersection destination6 = new Intersection(new BigInteger("975886496"),new Coordonnees(45.756874,4.8574047));
        ArrayList<Intersection> list = new ArrayList<Intersection>();
        list.add((destination5));
        list.add(destination3);
        list.add(destination4);
        list.add((destination6));
        return list;
    }

    public ArrayList<Livraison> creerListeLivraisons(ArrayList<Intersection> listeIntersections, long id, Creneau c){
        ArrayList<Livraison> listeLivraison= new ArrayList<Livraison>();
        for (Intersection i : listeIntersections){
            Livraison l =new Livraison((long)id, i, c);
            listeLivraison.add(l);
        }
        return listeLivraison;
    }
    public Tour calculerTour (Tour tour, Double vitesse, DonneesCarte carte, Intersection entrepot){
        tour.reinitialiserTrajet();
        final int creneau8=8;
        final int creneau9=9;
        final int creneau10=10;
        final int creneau11=11;

        ArrayList<Livraison> livraisons8 = new ArrayList<>();
        ArrayList<Livraison> livraisons9 = new ArrayList<>();
        ArrayList<Livraison> livraisons10 = new ArrayList<>();
        ArrayList<Livraison> livraisons11 = new ArrayList<>();

        List<Intersection> chemin8 = new ArrayList<>();
        List<Intersection> chemin9 = new ArrayList<>();
        List<Intersection> chemin10 = new ArrayList<>();
        List<Intersection> chemin11 = new ArrayList<>();

        ArrayList<Livraison> livraisons = tour.getLivraisons();

        for (Livraison l: livraisons) {
            if (l.getCreneau().getValeur()==creneau8){
                livraisons8.add(l);
            } else if (l.getCreneau().getValeur()==creneau9){
                livraisons9.add(l);
            } else if (l.getCreneau().getValeur()==creneau10){
                livraisons10.add(l);
            } else if (l.getCreneau().getValeur()==creneau11){
                livraisons11.add(l);
            }
        }

        //On trie les listes de livraison
        Intersection interCourante = entrepot;
        tour.ajouterIntersection(entrepot);
        LocalTime tempsCourant = LocalTime.of(8,0,0);
        double distance;


        livraisons8= Calculs.trierLivraisons(livraisons8,interCourante);
        for (Livraison l: livraisons8) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante,l.getAdresse(),carte.getCarte());
            distance = Calculs.getDistanceChemin(chemin);
            l.mettreAJourHeure(tempsCourant,distance,vitesse);
            if(l.getHeureArrivee().getHour()<(l.getCreneau().getValeur()+1) && chemin!=null){
                tour.ajouterListeAuTrajet(chemin);
                interCourante = l.getAdresse();
                tempsCourant=l.getHeureDepart();
            } else {
                l.livraisonNonLivree();
            }
        }

        livraisons9= Calculs.trierLivraisons(livraisons9,interCourante);
        for (Livraison l: livraisons9) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante,l.getAdresse(),carte.getCarte());
            distance = Calculs.getDistanceChemin(chemin);
            l.mettreAJourHeure(tempsCourant,distance,vitesse);
            if(l.getHeureArrivee().getHour()<(l.getCreneau().getValeur()+1) && chemin!=null){
                tour.ajouterListeAuTrajet(chemin);
                interCourante = l.getAdresse();
                tempsCourant=l.getHeureDepart();
            } else {
                l.livraisonNonLivree();
            }
        }

        livraisons10= Calculs.trierLivraisons(livraisons10,interCourante);

        for (Livraison l: livraisons10) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante,l.getAdresse(),carte.getCarte());
            distance = Calculs.getDistanceChemin(chemin);
            l.mettreAJourHeure(tempsCourant,distance,vitesse);
            if(l.getHeureArrivee().getHour()<(l.getCreneau().getValeur()+1) && chemin!=null){
                tour.ajouterListeAuTrajet(chemin);
                interCourante = l.getAdresse();
                tempsCourant=l.getHeureDepart();
            } else {
                l.livraisonNonLivree();
            }
        }

        livraisons11= Calculs.trierLivraisons(livraisons11,interCourante);
        for (Livraison l: livraisons11) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante,l.getAdresse(),carte.getCarte());
            distance = Calculs.getDistanceChemin(chemin);
            l.mettreAJourHeure(tempsCourant,distance,vitesse);
            if(l.getHeureArrivee().getHour()<(l.getCreneau().getValeur()+1) && chemin!=null){
                tour.ajouterListeAuTrajet(chemin);
                interCourante = l.getAdresse();
                tempsCourant=l.getHeureDepart();
            } else {
                l.livraisonNonLivree();
            }
        }

        if(interCourante!=entrepot){
            List<Intersection> chemin = Calculs.dijkstra(interCourante,entrepot,carte.getCarte());
            tour.ajouterListeAuTrajet(chemin);
            interCourante=entrepot;
        }


        return tour;
    }

    public void ouvrirDetails(String chemin, Long id){
        Tour t = catalogueTours.getTourById(id);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth() * 0.8;
        double height = 1020.0 / 1080.0 * screenBounds.getHeight();
        DetailsTour detailsTour = new DetailsTour(chemin,t,(int)(width), (int)(height));
        Stage stage = new Stage();
        try {
            detailsTour.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DonneesCarte getDonneesCarte() {
        return donneesCarte;
    }

    public CatalogueTours getCatalogueTours() {
        return catalogueTours;
    }

    public void setCatalogueTours(CatalogueTours catalogueTours) {
        this.catalogueTours = catalogueTours;
    }

    public void sauvegarderCatalogueTourXML (CatalogueTours Ctour, String cheminSauvegarde){
        System.out.println("Catalogue a sauvegardé : " +Ctour.toString());
        System.out.println("Chemin où enregistrer :" + cheminSauvegarde);
        FileSystemXML.EcrireCatalogueXML(Ctour,cheminSauvegarde);

    }
    public Livreur initialisationLivreur(String name){
        Livreur l = new Livreur(name);
        return l;
    }

    public ArrayList<Intersection> getAllIntersections() {
        // On crée une liste vide pour stocker les intersections
        ArrayList<Intersection> intersections = new ArrayList<>();
        // On parcourt l'ensemble des clés de la carte
        for (Intersection key : donneesCarte.getCarte().keySet()) {
            // On ajoute chaque clé à la liste
            intersections.add(key);
        }
        // On renvoie la liste
        return intersections;
    }

    public ArrayList<String> getListLivreur(){
        ArrayList<String> livreurs = new ArrayList<>();
        for (Tour t: catalogueTours.catalogue) {
            livreurs.add(t.getLivreur().getNom());
        }
        return livreurs;
    }

    public ArrayList<Tour> getTours(){
        return catalogueTours.getCatalogue();
    }

    public void setVueApplication(VueApplication vueApplication) {
        this.vueApplication = vueApplication;
    }

    public void setCarte(Carte carte){this.carte=carte;}
    public Carte getCarte(){return this.carte;}
    public void updateCarte(){
        vueApplication.updateCarte();
    }
    public void updatePanel(){
        vueApplication.updatePanel();
    }

    public void setNbLivreur(int nombre){

        if(nombre > catalogueTours.getListeLivreurs().size()){
            for(int i =catalogueTours.getListeLivreurs().size(); i<nombre;i++){
                catalogueTours.ajouterTour(new Tour(new Livreur(i)));
            }
        }else{
            for(int i = catalogueTours.getListeLivreurs().size(); i>nombre; i--){
                catalogueTours.supprimerTour();
            }

        }
    }

    public boolean restituerTour(String cheminfihcierCatalogueTour){
        boolean b = false;
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();
        Object[] objects = fsxml.lireXML(cheminfihcierCatalogueTour);
        if (objects[0]!=null && objects[0] instanceof CatalogueTours){
            CatalogueTours Ctour = (CatalogueTours) objects[0];
            if (this.nomFichierCarte.contains(Ctour.getMapName())){ //On vérifie la cohérence des cartes
                setCatalogueTours((CatalogueTours) objects[0]);
                b=true;
                updateCarte();
                updatePanel();
            } else {
                System.out.println("Impossible d'importer le Catalogue car le CatalogueTour ne correspond pas avec la carte affcihée");
            }
        }
        return b;
    }
}
