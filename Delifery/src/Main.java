// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import Util.FileSystemXML;
import Donnees.Intersection;
import Donnees.Section;
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        // Press Alt+Entrée with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        // Press Maj+F10 or click the green arrow button in the gutter to run the code.
        for (int i = 1; i <= 5; i++) {

            // Press Maj+F9 to start debugging your code. We have set one breakpoint
            // for you, but you can always add more by pressing Ctrl+F8.
            System.out.println("greg i = " + i);
        }
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
}