package Util;

import Donnees.Intersection;
import Util.Coordonnees;
import Donnees.Section;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
// Importer la classe DocumentBuilder pour construire un document XML
import javax.xml.parsers.DocumentBuilder;
// Importer la classe Document pour représenter le document XML
import org.w3c.dom.Document;
// Importer la classe Element pour représenter les éléments XML
import org.w3c.dom.Element;
// Importer la classe NodeList pour représenter les listes de nœuds XML
import org.w3c.dom.NodeList;
//Importer la classe Node pour représenter les nœuds XML
import org.w3c.dom.Node;
// Importer la classe Exception pour gérer les éventuelles erreurs
import java.lang.Exception;
import java.math.BigInteger;

public class FileSystemXML {

    public FileSystemXML(){};
    // Définir une méthode statique qui prend en paramètre le chemin du fichier XML et qui renvoie un objet Warehouse, un tableau d'objets Intersection et un tableau d'objets Segment
    public static Object[] lireXML(String chemin) {
        // Déclarer un objet Warehouse, un tableau d'objets Intersection et un tableau d'objets Segment
        Intersection[] warehouse = null;
        Intersection[] intersections = null;
        Section[] sections = null;
        float minLong = 100;
        float minLat = 100;
        float maxLong = 0;
        float maxLat = 0;


        try {

            // Créer un objet File à partir du chemin du fichier XML
            File fichier = new File(chemin);
            // Créer un objet DocumentBuilderFactory pour créer un parseur XML
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // Créer un objet DocumentBuilder à partir de l'objet DocumentBuilderFactory
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Créer un objet Document à partir de l'objet File en utilisant la méthode parse de l'objet DocumentBuilder
            Document doc = db.parse(fichier);
            // Normaliser le document XML en utilisant la méthode normalize de l'objet Document
            doc.normalize();
            // Récupérer l'élément racine du document XML en utilisant la méthode getDocumentElement de l'objet Document
            Element racine = doc.getDocumentElement();

            // Vérifier que l'élément racine est bien <map>
            if (racine.getNodeName().equals("map")) {
                // Récupérer la liste des éléments <intersection> en utilisant la méthode getElementsByTagName de l'objet Element
                NodeList listeIntersection = racine.getElementsByTagName("intersection");
                // Récupérer la liste des éléments <segment> en utilisant la méthode getElementsByTagName de l'objet Element
                NodeList listeSection = racine.getElementsByTagName("segment");
                // Récupérer la liste des éléments <warehouse> en utilisant la méthode getElementsByTagName de l'objet Element
                NodeList listeWareHouse = racine.getElementsByTagName("warehouse");

                // Créer un tableau d'objets Intersection de la même taille que la liste des éléments <intersection> en utilisant la méthode getLength de l'objet NodeList
                intersections = new Intersection[listeIntersection.getLength()];
                // Créer un tableau d'objets Section de la même taille que la liste des éléments <segment> en utilisant la méthode getLength de l'objet NodeList
                sections = new Section[listeSection.getLength()];
                // Créer un tableau d'objets Section de la même taille que la liste des éléments <warehouse> en utilisant la méthode getLength de l'objet NodeList
                warehouse = new Intersection[listeWareHouse.getLength()];


                // Parcourir la liste des éléments <intersection> avec une boucle for
                for (int i = 0; i < listeIntersection.getLength(); i++) {
                    // Récupérer le i-ème élément <intersection> en utilisant la méthode item de l'objet NodeList
                    Element elementIntersection = (Element) listeIntersection.item(i);
                    // Récupérer les attributs id, latitude et longitude de l'élément <intersection> en utilisant la méthode getAttribute de l'objet Element
                    String id = elementIntersection.getAttribute("id");
                    String latitude = elementIntersection.getAttribute("latitude");
                    if(minLat> Float.parseFloat(latitude))
                    {
                      minLat = Float.parseFloat(latitude);
                    }
                    if (maxLat< Float.parseFloat(latitude)) {
                        maxLat = Float.parseFloat(latitude);
                    }

                    String longitude = elementIntersection.getAttribute("longitude");
                    if(minLong> Float.parseFloat(longitude))
                    {
                        minLong = Float.parseFloat(longitude);
                    }
                    if (maxLong< Float.parseFloat(longitude)) {
                        maxLong = Float.parseFloat(longitude);
                    }

                    Coordonnees coord = new Coordonnees(Double.parseDouble(latitude),Double.parseDouble(longitude));
                    Intersection intersection = new Intersection(new BigInteger(id), coord);
                    intersections[i] = intersection;

                }


                for (int i = 0; i < listeSection.getLength(); i++) {
                    // Récupérer le i-ème élément <intersection> en utilisant la méthode item de l'objet NodeList
                    Element elementSection = (Element) listeSection.item(i);
                    // Récupérer les attributs id, latitude et longitude de l'élément <intersection> en utilisant la méthode getAttribute de l'objet Element
                    String idDestination = elementSection.getAttribute("destination");
                    String longueur = elementSection.getAttribute("length");
                    String nom = elementSection.getAttribute("name");
                    String idOrigine = elementSection.getAttribute("origin");
                    //System.out.println(idDestination+";"+longueur+";"+nom+";"+idOrigine);

                    //Recherche des intersections d'origine et de destination de la section
                    Intersection intersectionDestination = null;
                    Intersection intersectionOrigine = null;
                    int ok = 0;
                    for (Intersection inter : intersections) {
                        if (inter.getId().equals(new BigInteger(idDestination))) {
                            intersectionDestination = inter;
                            ok++;
                            if (ok==2){
                                break;
                            }
                        } else if (inter.getId().equals(new BigInteger(idOrigine))) {
                            intersectionOrigine = inter;
                            ok++;
                            if (ok==2){
                                break;
                            }
                        }
                    }
                    Section section = new Section(Float.parseFloat(longueur), nom, intersectionOrigine,intersectionDestination);
                    sections[i] = section;
                    System.out.println(section.toString());
                    //System.out.println(section.toString());
                }

                for (int i = 0; i < listeWareHouse.getLength(); i++) {
                    // Récupérer le i-ème élément <intersection> en utilisant la méthode item de l'objet NodeList
                    Element elementSection = (Element) listeWareHouse.item(i);
                    String idAdresse = elementSection.getAttribute("address");
                    Intersection intersectionWH = null;
                    for (Intersection inter : intersections) {
                        if (inter.getId().equals(new BigInteger(idAdresse))) {
                            intersectionWH = inter;
                            break;
                        }
                    }
                    Intersection WareHouse = new Intersection(intersectionWH);
                    warehouse[i]=WareHouse;
                }
            }
        } catch (Exception e) {
            // Afficher le message d'erreur en cas d'exception
            System.out.println(e.getMessage());
            System.out.println("Erreur");
        }

        // Retourner un tableau d'objets contenant l'objet Warehouse, le tableau d'objets Intersection et le tableau d'objets Segment
        return new Object[] {warehouse, intersections, sections, minLong, maxLong, minLat,maxLat};
    }

}
