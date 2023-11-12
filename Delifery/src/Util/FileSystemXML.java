package Util;

import Donnees.*;
import Util.Coordonnees;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.lang.Exception;
import java.math.BigInteger;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FileSystemXML {

    public FileSystemXML(){};
    // Définir une méthode statique qui prend en paramètre le chemin du fichier XML et qui renvoie un objet Warehouse, un tableau d'objets Intersection et un tableau d'objets Segment
    public static Object[] lireXML(String chemin) {

        Object[] returnObject = null;

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
            doc.getDocumentElement().normalize();
            // Récupérer l'élément racine du document XML en utilisant la méthode getDocumentElement de l'objet Document
            Element racine = doc.getDocumentElement();

            // Vérifier que l'élément racine est bien <map>
            if (racine.getNodeName().equals("map")) returnObject = importMap(racine);
            else if (racine.getNodeName().equals("catalogue")) returnObject = importTour(racine);
        } catch (Exception e) {
            // Afficher le message d'erreur en cas d'exception
            System.out.println(e.getMessage());
            System.out.println("Erreur");
        }

        // Retourner un tableau d'objets contenant l'objet Warehouse, le tableau d'objets Intersection et le tableau d'objets Segment
        return returnObject;
    }

    private static Object[] importMap(Element racine) {
        // Déclarer un objet Warehouse, un tableau d'objets Intersection et un tableau d'objets Segment
        Intersection[] warehouse = null;
        Intersection[] intersections = null;
        Section[] sections = null;
        float minLong = 100;
        float minLat = 100;
        float maxLong = 0;
        float maxLat = 0;
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
            //System.out.println(section.toString());
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

        return new Object[] {warehouse, intersections, sections, minLong, maxLong, minLat,maxLat};
    }

    public static Object[] importTour (Element racine) {
        CatalogueTours catalogueTours = new CatalogueTours(racine.getAttribute("idMap"));

        NodeList toursList = racine.getElementsByTagName("tour");
        for (int i = 0; i < toursList.getLength(); i++) {
            Node tourNode = toursList.item(i);

            if(tourNode.getNodeType() == Node.ELEMENT_NODE) {
                Element tour = (Element) toursList.item(i);
                Tour tourToAdd = new Tour(Long.parseLong(tour.getAttribute("id")));
                Node livraisons = tour.getElementsByTagName("livraisons").item(0);
                Node trajet = tour.getElementsByTagName("trajet").item(0);

                NodeList livraisonList = ((Element)(livraisons)).getElementsByTagName("livraison");
                NodeList intersectionList = ((Element)(trajet)).getElementsByTagName("intersection");

                for(int j = 0; j < intersectionList.getLength(); j++) {
                    Element intersectionElement = (Element) intersectionList.item(j);

                    BigInteger id = new BigInteger(intersectionElement.getAttribute("id"));
                    Double latitude = new Double(intersectionElement.getAttribute("latitude"));
                    Double longitude = new Double(intersectionElement.getAttribute("longitude"));
                    Coordonnees coordonnees = new Coordonnees(latitude, longitude);
                    Intersection intersection = new Intersection(id, coordonnees);

                    tourToAdd.ajouterIntersection(intersection);
                }

                for(int k = 0; k < livraisonList.getLength(); k++) {
                    Element livraisonElement = (Element)(livraisonList.item(k));

                    Long idLivraison = new Long(livraisonElement.getAttribute("id"));
                    String idAdresse = livraisonElement.getAttribute("idIntersection");
                    Intersection adresse = getIntersectionById(tourToAdd.getTrajet(), idAdresse);
                    LocalTime heureDepart = parseTime(livraisonElement.getAttribute("heureDepart"));
                    LocalTime heureArrivee = parseTime(livraisonElement.getAttribute("heureArrivee"));
                    Creneau creneau = Creneau.valueOf(livraisonElement.getAttribute("creneau"));
                    Livraison livraison = new Livraison(idLivraison, adresse, creneau, heureDepart, heureArrivee);

                    tourToAdd.addLivraisonsTour(livraison);
                }
                catalogueTours.catalogue.add(tourToAdd);
            }

        }

        return new Object[] {catalogueTours};
    }

    private static Intersection getIntersectionById(ArrayList<Intersection> intersections, String id) {
        BigInteger bigId = new BigInteger(id);

        for(Intersection intersection : intersections) {
            if (intersection.getId().equals(bigId)) return intersection;
        }
        return null;
    }

    private static LocalTime parseTime(String timeString) {
        // Define the formatter for the given time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Parse the time string and return a LocalTime instance
        return LocalTime.parse(timeString, formatter);
    }

    // Une méthode pour écrire un catalogue de tours dans un fichier XML
    public static void EcrireCatalogueXML(CatalogueTours c, String chemin, String nom) {
        try {
            // Créer un fichier XML avec le chemin et le nom spécifiés
            File file = new File(chemin + "/" + nom +".xml");

            // Créer un résultat de type fichier
            StreamResult result = new StreamResult(file);

            // Créer un document XML vide
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            // Créer l'élément racine <catalogue>
            Element catalogue = doc.createElement("catalogue");
            catalogue.setAttribute("idMap", c.mapName);
            doc.appendChild(catalogue);

            // Parcourir la liste des tours du catalogue
            for (Tour t : c.getCatalogue()) {
                // Créer un élément <tour> avec l'id du tour comme attribut
                Element tour = doc.createElement("tour");
                tour.setAttribute("id", t.getId().toString());
                catalogue.appendChild(tour);

                // Créer un élément <livreur> avec le nom et la vitesse du livreur comme attributs
                Element livreur = doc.createElement("livreur");
                livreur.setAttribute("nom", t.getLivreur().getNom());
                livreur.setAttribute("vitesse", String.valueOf(t.getLivreur().getVitesse()));
                tour.appendChild(livreur);

                // Créer un élément <livraisons>
                Element livraisons = doc.createElement("livraisons");
                tour.appendChild(livraisons);

                // Parcourir la liste des livraisons du tour
                for (Livraison l : t.getLivraisons()) {
                    // Créer un élément <livraison> avec l'id, l'adresse, le créneau, l'heure d'arrivée et l'heure de départ de la livraison comme attributs
                    Element livraison = doc.createElement("livraison");
                    livraison.setAttribute("id", l.getId().toString());
                    livraison.setAttribute("idIntersection", l.getAdresse().getId().toString());
                    livraison.setAttribute("creneau", l.getCreneau().toString());
                    livraison.setAttribute("heureArrivee", l.getHeureArrivee().toString());
                    livraison.setAttribute("heureDepart", l.getHeureDepart().toString());
                    livraisons.appendChild(livraison);
                }

                // Créer un élément <trajet>
                Element trajet = doc.createElement("trajet");
                tour.appendChild(trajet);

                // Parcourir la liste des intersections du trajet
                for (Intersection i : t.getTrajet()) {
                    // Créer un élément <intersection> avec l'id, la latitude et la longitude de l'intersection comme attributs
                    Element intersection = doc.createElement("intersection");
                    intersection.setAttribute("id", i.getId().toString());
                    intersection.setAttribute("latitude", i.getCoordonnees().getLatitude().toString());
                    intersection.setAttribute("longitude", i.getCoordonnees().getLongitude().toString());
                    trajet.appendChild(intersection);
                }
            }

            // Transformer le document XML en source DOM
            DOMSource source = new DOMSource(doc);

            try {
                // Créer un transformateur XML
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();

                // Définir les propriétés du fichier XML (indentation, encodage, etc.)
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                try {
                    // Transformer la source DOM en résultat fichier
                    transformer.transform(source, result);

                    // Afficher un message de succès
                    //System.out.println("Le fichier XML a été créé avec succès.");
                } catch (TransformerException e) {
                    // Afficher un message d'erreur
                    //System.out.println("Une erreur est survenue lors de la transformation du document XML.");
                    e.printStackTrace();
                }
            } catch (TransformerException e) {
                // Afficher un message d'erreur
                //System.out.println("Une erreur est survenue lors de la création du transformateur XML.");
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            // Afficher un message d'erreur
            //System.out.println("Une erreur est survenue lors de la configuration du parseur XML.");
            e.printStackTrace();
        }
    }
}
