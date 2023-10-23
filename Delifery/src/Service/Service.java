package Service;
import Donnees.Intersection;
import Donnees.Coordonnees;
import Donnees.Section;
import Util.FileSystemXML;
public class Service {
    public Service(){

    }

    public Object[] lireFichierXML(String chemin){
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();
        Object[] objects = fsxml.lireXML(chemin);
        Intersection[] WareHouse = (Intersection[]) objects[0];
        Intersection[] intersections = (Intersection[]) objects[1];
        Section[] sections = (Section[]) objects[2];

        return objects;
    }
}
