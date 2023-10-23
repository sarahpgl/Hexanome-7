package Service;
import Donnees.Intersection;
import Donnees.Section;
import Util.FileSystemXML;
import Vue.FenetreLancement;
import Vue.MaVue;

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

    public void afficherMap() {
        // Créez une instance de la classe Vue
        MaVue maVue = new MaVue();
        maVue.ouvrirFenetre();
    }
}
