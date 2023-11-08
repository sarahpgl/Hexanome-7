package Service;

import Donnees.*;
import Util.Calculs;
import Util.Coordonnees;
import Util.FileSystemXML;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.Map;
import java.util.HashMap;
import Vue.FenetreLancement;

import java.time.LocalTime;

import java.util.*;


public class Service {

    private static volatile Service instance;
    private DonneesCarte donneesCarte;
    private CatalogueTours catalogueTours;


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


        // Chemin d'accès fixe (à modifier selon vos besoins)
        String cheminFixe = System.getProperty("user.dir") + "/Delifery/fichiersXML2022/";

        String cheminComplet = cheminFixe + nomFichier;

        System.out.println("Chemin Fixe (Methode creerDonneesCarte dans Service) : "+cheminComplet);

        Object[] objects = fsxml.lireXML(nomFichier);

        Intersection[] entrepot = (Intersection[]) objects[0];

        if(entrepot==null){
            objects = fsxml.lireXML(cheminComplet);
            entrepot = (Intersection[]) objects[0];
        }


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

    public Tour creerTour(Long id,ArrayList<Livraison> livraisons, ArrayList<Intersection> intersections){

        Tour tour = new Tour(id, livraisons, intersections);

        return tour;
    }
    public Boolean essaieAjoutLivraisonAuTour(Intersection adresse, Creneau creneau, Livreur Livreur){
        Livraison l = new Livraison((long)(Math.random()*1000),adresse,  creneau);
        // chercher le tour auquel correspond le livreur

        // essayer de calculer le tour (modifier dans la fonction qui calcule le tour pour renvoyer l'état de l'opération)
        // si ok
        return true;
        // si non return false
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

        return tour;
    }

    public DonneesCarte getDonneesCarte() {
        return donneesCarte;
    }

    public CatalogueTours getCatalogueTours() {
        return catalogueTours;
    }

    public void sauvegarderCatalogueTourXML (CatalogueTours Ctour, String chemin, String nomFichier){
        FileSystemXML.EcrireCatalogueXML(Ctour,chemin,nomFichier);
    }
}
