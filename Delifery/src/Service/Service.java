package Service;
import Donnees.DonneesCarte;
import Donnees.Intersection;
import Donnees.Section;
import Util.FileSystemXML;
import Vue.MaVue;
import java.util.Map;
import java.util.HashMap;
public class Service {
    public Service(){

    }
    public DonneesCarte creerDonneesCarte(String nomFichier) {
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();


        // Chemin d'accès fixe (à modifier selon vos besoins)
        String cheminFixe = System.getProperty("user.dir") + "/fichiersXML2022/";

        // Combinez le chemin fixe et le nom du fichier
        String cheminComplet = cheminFixe + nomFichier;
        Object[] objects = fsxml.lireXML(cheminComplet);

        Intersection[] entrepot = (Intersection[]) objects[0];


        Intersection[] intersections = (Intersection[]) objects[1];
        Section[] sections = (Section[]) objects[2];
        float minLong = (float) objects[3];
        float maxLong = (float) objects[4];
        float minLat = (float) objects[5];
        float maxLat = (float) objects[6];
        float longDiff = maxLong - minLong;
        float latDiff = maxLat - minLat;
        float echelleX = latDiff/1920;
        float echelleY = longDiff/1080;
//1920//1080

        Intersection entrepotDepart = entrepot[0];
        Map<Intersection, Map<Intersection, Float>> carte = new HashMap<>();


        for (Section s : sections) {
            Intersection origine = s.getOrigine();
            Intersection destination = s.getDestination();
            Float taille = s.getTaille();

            // Vérifiez si la clé origine existe dans la carte
            if (!carte.containsKey(origine)) {
                carte.put(origine, new HashMap<>());
            }
            carte.get(origine).put(destination, taille);
        }

        DonneesCarte carteCourante = new DonneesCarte(nomFichier, entrepotDepart, carte,echelleX,echelleY);
        return carteCourante;
    }
}
