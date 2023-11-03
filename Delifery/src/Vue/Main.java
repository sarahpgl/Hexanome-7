package Vue;// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import Util.Coordonnees;
import Util.FileSystemXML;
import Donnees.Intersection;
import Donnees.Section;

import java.math.BigInteger;
import java.util.Arrays;
import Service.Service;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import Donnees.DonneesCarte;

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
        //launch(FenetreLancement.class, args);

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

        Service service = new Service();
        DonneesCarte carte = service.creerDonneesCarte(nomFichier);

        // Créez une instance de la classe Vue
        MaVue maVue = new MaVue();
        maVue.ouvrirFenetreVueApp();
    }

    public static void TestOuvrirPageLancement() {
        MaVue maVue = new MaVue();
        maVue.ouvrirFenetreLancement();
    }

    public static void TesterDijkstra(){

        Service service = new Service();
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
        } else {
            System.out.print("Le chemin est null");
        }
    }

    public static void TestCreerCarte(){
        Service service = new Service();
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
            // Vous pouvez également afficher d'autres informations si nécessaire, par exemple les coordonnées.
        }
    }
}
