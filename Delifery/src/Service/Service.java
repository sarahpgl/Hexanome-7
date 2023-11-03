package Service;

import Donnees.*;
import Util.FileSystemXML;
import java.util.Map;
import java.util.HashMap;
import Vue.FenetreLancement;
import Vue.MaVue;

import java.util.*;


public class Service {
    public Service() {

    }

    public DonneesCarte creerDonneesCarte(String nomFichier) {
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();


        // Chemin d'accès fixe (à modifier selon vos besoins)
        String cheminFixe = System.getProperty("user.dir") + "/Delifery/fichiersXML2022/";

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
        return carteCourante;
    }

    public Tour calculerTour (Tour tour){
        final int creneau8=8;
        final int creneau9=9;
        final int creneau10=10;
        final int creneau11=11;

        ArrayList<Intersection> livraisons8 = new ArrayList<>();
        ArrayList<Intersection> livraisons9 = new ArrayList<>();
        ArrayList<Intersection> livraisons10 = new ArrayList<>();
        ArrayList<Intersection> livraisons11 = new ArrayList<>();

        List<Intersection> chemin8 = new ArrayList<>();
        List<Intersection> chemin9 = new ArrayList<>();
        List<Intersection> chemin10 = new ArrayList<>();
        List<Intersection> chemin11 = new ArrayList<>();

        return tour;
    }
}
