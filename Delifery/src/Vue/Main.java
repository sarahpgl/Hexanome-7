package Vue;// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import Donnees.*;
import Util.Coordonnees;
import Util.FileSystemXML;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.*;

import Service.Service;

import Util.Calculs;
import static javafx.application.Application.launch;


public class Main {
    public static void main(String[] args) {
        // Press Alt+Entrée with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it
        //TestOuvrirPageLancement();
        //TesterDijkstra();
        //TestCreerCarte();
        //TestLireXML();

        testerDetailTour();
        launch(FenetreLancement.class, args);


        //testerDetailTour();

        //TesterTrie();
        //testerHeureLivraison();
        //testerCalculTour();
        //getInterById();
        //TesterCreationXMLCatalogueTour();
    }

    public static void TestLireXML(){
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();
        System.out.println("Voici le chemin du dossier courant : " + System.getProperty("user.dir"));
        Object[] objects = fsxml.lireXML(System.getProperty("user.dir")+"/Delifery/fichiersXML2022/smallMap.xml");
        Intersection[] WareHouse = (Intersection[]) objects[0];
        Intersection[] intersections = (Intersection[]) objects[1];
        Section[] sections = (Section[]) objects[2];
        // Afficher le tableau d'objets Intersection en utilisant la méthode toString de la classe Arrays
        System.out.println("Les intersections sont : " + Arrays.toString(intersections));
        System.out.println("Les sections sont : " + Arrays.toString(sections));
        System.out.println("La WareHouse est : " + Arrays.toString(WareHouse));
    }

    public static void TestOuvrirVueApplication(){
        System.out.printf("Hello and welcome!");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Veuillez entrer le nom du ficher XML : ");
        String nomFichier = scanner.nextLine();

        Service service = Service.getInstance();
        DonneesCarte carte = service.creerDonneesCarte(nomFichier);
    }

    public static void TesterDijkstra(){

        Service service = Service.getInstance();
        System.out.println("Nom du fichier xml : " +System.getProperty("user.dir")+"/Delifery/fichiersXML2022/smallMap.xml");
        DonneesCarte carte = service.creerDonneesCarte("mediumMap.xml");
        Intersection Entrepot = new Intersection(new BigInteger("25303831"),new Coordonnees(45.74979,4.87572));
        Intersection destination1 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214,4.875591));
        Intersection destination2 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969,4.873468));
        Intersection destination3 = new Intersection(new BigInteger("25321422"), new Coordonnees(45.749027,4.873145));
        Intersection destination4 = new Intersection(new BigInteger("975886496"),new Coordonnees(45.756874,4.8574047));
        List<Intersection> chemin = Calculs.dijkstra(Entrepot,destination1,carte.getCarte());
        if (chemin != null) {
            afficherChemin(chemin);
            System.out.println("Distance : " + Calculs.getDistanceChemin(chemin)+ " m ");
            System.out.println("Distance : " + Entrepot.getDistanceTo(destination1)+ " m ");
        } else {
            System.out.print("Le chemin est null");
        }
    }

    public static void TestCreerCarte(){
        Service service = Service.getInstance();
        DonneesCarte carte = service.creerDonneesCarte("smallMap.xml");
        afficherGraphe(carte.getCarte());
    }

    public static void afficherGraphe(Map<Intersection, Map<Intersection, Float>> graphe) {
        for (Map.Entry<Intersection, Map<Intersection, Float>> entry : graphe.entrySet()) {
            Intersection depart = entry.getKey();
            Map<Intersection, Float> voisins = entry.getValue();

            System.out.println("Intersection de départ : " + depart.getId());

            for (Map.Entry<Intersection, Float> voisinEntry : voisins.entrySet()) {
                Intersection voisin = voisinEntry.getKey();
                Float longueur = voisinEntry.getValue();

                System.out.println("  Intersection accessible : " + voisin.getId());
                System.out.println("  Longueur : " + longueur);
            }
            System.out.println();
        }
    }
    public static void afficherChemin(List<Intersection> chemin) {
        if (chemin == null) {
            System.out.println("Aucun chemin n'existe.");
            return;
        }
        int i=0;
        for (Intersection intersection : chemin) {
            if(i==0){
                System.out.print("Intersection "+i+" : " + intersection.getId());
            }else {
                System.out.println(" | Intersection "+i+" : " + intersection.getId());
            }
            i++;
        }
    }

    public static void TesterTrie(){
        Service service = Service.getInstance();
        System.out.println("Nom du fichier xml : " +System.getProperty("user.dir")+"/Delifery/fichiersXML2022/smallMap.xml");
        DonneesCarte carte = service.creerDonneesCarte("mediumMap.xml");
        Intersection Entrepot = new Intersection(new BigInteger("25303831"),new Coordonnees(45.74979,4.87572));
        Intersection destination1 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214,4.875591));
        Intersection destination2 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969,4.873468));
        Intersection destination3 = new Intersection(new BigInteger("25321422"), new Coordonnees(45.749027,4.873145));
        Intersection destination4 = new Intersection(new BigInteger("975886496"),new Coordonnees(45.756874,4.8574047));
        Livraison livraison1 = new Livraison((long) 1,destination1);
        Livraison livraison2 = new Livraison((long) 2,destination2);
        Livraison livraison3 = new Livraison((long) 3,destination3);
        Livraison livraison4 = new Livraison((long) 4,destination4);
        ArrayList<Livraison> livraisons = new ArrayList<>();
        livraisons.add(livraison4);
        livraisons.add(livraison2);
        livraisons.add(livraison1);
        livraisons.add(livraison3);

        System.out.println();
        System.out.println("Livraisons non triées :");
        for (Livraison l: livraisons) {
            System.out.println(l.getAdresse().toString());
        }

        List<Livraison> livraisonsTriees = Calculs.trierLivraisons(livraisons,Entrepot);

        System.out.println();
        System.out.println("Livraisons triées :");
        for (Livraison l: livraisonsTriees) {
            System.out.println(l.getAdresse().toString());
        }
    }

    public static void testerHeureLivraison(){
        Service service = Service.getInstance();
        DonneesCarte carte = service.creerDonneesCarte("mediumMap.xml");
        Intersection Entrepot = new Intersection(new BigInteger("25303831"),new Coordonnees(45.74979,4.87572));
        Intersection destination1 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214,4.875591));
        Livraison livraison1 = new Livraison((long) 1,destination1);
        LocalTime heureDepartEntrepot = LocalTime.of(7,55,0);
        List<Intersection> chemin = Calculs.dijkstra(Entrepot,destination1,carte.getCarte());
        double distance = Calculs.getDistanceChemin(chemin);
        afficherChemin(chemin);
        System.out.println("Distance du chemin :"+distance);
        livraison1.mettreAJourHeure(heureDepartEntrepot,distance,15);
        System.out.println("Heure d'arrivée :"+livraison1.getHeureArrivee());
        System.out.println("Heure de départ :"+livraison1.getHeureDepart());
    }

    /*public static void testerCalculTour(){
        Service service = Service.getInstance();
        DonneesCarte carte = service.creerDonneesCarte("mediumMap.xml");
        Intersection Entrepot = new Intersection(new BigInteger("25303831"),new Coordonnees(45.74979,4.87572));
        Intersection destination1 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214,4.875591));
        Intersection destination2 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969,4.873468));
        Intersection destination3 = new Intersection(new BigInteger("25321422"), new Coordonnees(45.749027,4.873145));
        Intersection destination4 = new Intersection(new BigInteger("975886496"),new Coordonnees(45.756874,4.8574047));
        Livraison livraison1 = new Livraison((long) 1,destination1, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison2 = new Livraison((long) 2,destination2, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison3 = new Livraison((long) 3,destination3, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison4 = new Livraison((long) 4,destination4, Creneau.valueOf("HUIT_NEUF"));
        ArrayList<Livraison> livraisons = new ArrayList<>();
        livraisons.add(livraison4);
        livraisons.add(livraison2);
        livraisons.add(livraison1);
        livraisons.add(livraison3);
        Tour tour = new Tour((long) 1, livraisons);
        tour = service.calculerTour(tour,(double)15,carte,Entrepot);
        System.out.println(tour.toString());
    }*/

    public static void getInterById(){
        Service service = Service.getInstance();
        DonneesCarte carte = service.creerDonneesCarte("mediumMap.xml");
        System.out.println(carte.findIntersectionById("975886496").toString());
    }

    public static void TesterCreationXMLCatalogueTour(){
        Service service = Service.getInstance();
        DonneesCarte carte = service.creerDonneesCarte("mediumMap.xml");
        Intersection Entrepot = new Intersection(new BigInteger("25303831"),new Coordonnees(45.74979,4.87572));
        Intersection destination1 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214,4.875591));
        Intersection destination2 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969,4.873468));
        Intersection destination3 = new Intersection(new BigInteger("25321422"), new Coordonnees(45.749027,4.873145));
        Intersection destination4 = new Intersection(new BigInteger("975886496"),new Coordonnees(45.756874,4.8574047));
        Livraison livraison1 = new Livraison((long) 1,destination1, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison2 = new Livraison((long) 2,destination2, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison3 = new Livraison((long) 3,destination3, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison4 = new Livraison((long) 4,destination4, Creneau.valueOf("HUIT_NEUF"));
        ArrayList<Livraison> livraisons = new ArrayList<>();
        livraisons.add(livraison4);
        livraisons.add(livraison2);
        livraisons.add(livraison1);
        livraisons.add(livraison3);
        Tour tour = new Tour( livraisons, new Livreur("Maria"));
        tour = service.calculerTour(tour,(double)15,carte,Entrepot);
        CatalogueTours c = new CatalogueTours();
        c.ajouterTour(tour);
        FileSystemXML.EcrireCatalogueXML(c,"Delifery/output","Test");
    }

    public static void testerDetailTour(){
        Service service = Service.getInstance();
        DonneesCarte carte = service.creerDonneesCarte("mediumMap.xml");
        Intersection Entrepot = new Intersection(new BigInteger("25303831"),new Coordonnees(45.74979,4.87572));
        //premier tour
        Intersection destination1 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969,4.873468));
        Intersection destination2 = new Intersection(new BigInteger("1957527553"), new Coordonnees(45.736317,4.8697796));
        //Intersection destination3 = new Intersection(new BigInteger("25321422"), new Coordonnees(45.749027,4.873145));
        Intersection destination4 = new Intersection(new BigInteger("21703594"),new Coordonnees(45.73886,4.876077));
        Livraison livraison1 = new Livraison((long) 1,destination1, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison2 = new Livraison((long) 2,destination2, Creneau.valueOf("HUIT_NEUF"));
        //Livraison livraison3 = new Livraison((long) 3,destination3, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison4 = new Livraison((long) 4,destination4, Creneau.valueOf("HUIT_NEUF"));
        ArrayList<Livraison> livraisons = new ArrayList<>();
        livraisons.add(livraison4);
        livraisons.add(livraison2);
        livraisons.add(livraison1);
        //livraisons.add(livraison3);
        Tour tour = new Tour( livraisons, new Livreur("Moïse"));

        //deuxieme tour
        Intersection destination5 = new Intersection(new BigInteger("1682387628"), new Coordonnees(45.754463,4.882047));
        Intersection destination6 = new Intersection(new BigInteger("60901950"), new Coordonnees(45.7369,4.89531));
        Intersection destination7 = new Intersection(new BigInteger("459797854"), new Coordonnees(45.76118,4.898195));
        Livraison livraison5 = new Livraison((long) 5,destination5, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison6 = new Livraison((long) 6,destination6, Creneau.valueOf("HUIT_NEUF"));
        Livraison livraison7 = new Livraison((long) 7,destination7, Creneau.valueOf("HUIT_NEUF"));
        ArrayList<Livraison> livraisons2 = new ArrayList<>();
        livraisons2.add(livraison5);
        livraisons2.add(livraison6);
        livraisons2.add(livraison7);
        Tour tour2 = new Tour( livraisons2, new Livreur("Adam Labrosse"));

        tour = service.calculerTour(tour,(double)15,carte,Entrepot);
        tour2 = service.calculerTour(tour2,(double)15,carte,Entrepot);

        CatalogueTours c = new CatalogueTours("mediumMap.xml");
        ArrayList<Tour> toursTest = c.getCatalogue();
        c.ajouterTour(tour);
        c.ajouterTour(tour2);

        service.setCatalogueTours(c);
        System.out.println("iciiii"+tour2.toString());

    }

    public static void testImportTours() {
        String filePath = System.getProperty("user.dir") + "/Delifery/fichiersXML2022/Test.xml";
        System.out.println("File Path to import tours : " + filePath);
        Object[] test = FileSystemXML.lireXML(filePath);
        System.out.println(test[0]);
    }
}
