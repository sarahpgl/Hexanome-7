package Vue;// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import Util.FileSystemXML;
import Donnees.Intersection;
import Donnees.Section;
import java.util.Arrays;
import Service.Service;
import java.util.Scanner;
import Donnees.DonneesCarte;
public class Main {
    public static void main(String[] args) {
        // Press Alt+Entrée with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        /*System.out.printf("Hello and welcome!");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Veuillez entrer le nom du ficher XML : ");
        String nomFichier = scanner.nextLine();


        Service service = new Service();
        DonneesCarte carte = service.creerDonneesCarte(nomFichier);

        // Créez une instance de la classe Vue
        //MaVue maVue = new MaVue();
       // maVue.ouvrirFenetre();

        // Chemin d'accès fixe (à modifier selon vos besoins)
        String cheminFixe = System.getProperty("user.dir")+"/Delifery/fichiersXML2022/";

        // Combinez le chemin fixe et le nom du fichier
        String cheminComplet = cheminFixe + nomFichier;*/

       // MaVue vue = new MaVue();
        //vue.ouvrirFenetre();
        //Object[] objects = service.lireFichierXML(cheminComplet);



        /*FileSystemXML fsxml;
        fsxml = new FileSystemXML();
        System.out.println("Voici le chemin du dossier courant : " + System.getProperty("user.dir"));
        Object[] objects = fsxml.lireXML(System.getProperty("user.dir")+"/Delifery/fichiersXML2022/smallMap.xml");
        Intersection[] WareHouse = (Intersection[]) objects[0];
        Intersection[] intersections = (Intersection[]) objects[1];
        Section[] sections = (Section[]) objects[2];
        // Afficher le tableau d'objets Intersection en utilisant la méthode toString de la classe Arrays
        System.out.println("Les intersections sont : " + Arrays.toString(intersections));
        System.out.println("Les sections sont : " + Arrays.toString(sections));
        System.out.println("La WareHouse est : " + Arrays.toString(WareHouse));*/
        Carte c = new Carte();
        c.creerGraphe("smallMap.xml");

    }
}