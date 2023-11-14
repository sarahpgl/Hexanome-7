package Vue;

import Donnees.*;
import Service.Service;
import Util.Calculs;
import Util.Coordonnees;
import javafx.animation.PauseTransition;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Carte extends Pane {

    //nb de tours au total à afficher
    int nbTours = -1;

    List<ElementListe> listeCouleurs = new ArrayList<>();

    Intersection entrepot;
    float[] util;
    Service service = Service.getInstance();
    Integer panelWidth;
    Integer panelHeight;
    Tour tourAAfficher = null;
    private Map<Intersection, Map<Intersection, Float>> graph;
    private String cheminFichier;

    /**
     * Constructeur de Carte, Affichage graphique du graph et des Tours
     * Utilisé notamment pour l'affichage du détail d'un tour
     * @param cheminFich chemin vers le fichier de la carte
     * @param panelWidth Largeur du Panneau
     * @param panelHeight Hauteur du Panneau
     * @param tourAAfficher le Tour à afficher
     */
    public Carte(String cheminFich, Integer panelWidth, Integer panelHeight, Tour tourAAfficher) {
        super();
        this.cheminFichier = cheminFich;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;
        this.tourAAfficher = tourAAfficher;


        // Create a sample graph
        DonneesCarte dc = service.creerDonneesCarte(cheminFichier);
        graph = dc.getCarte();
        entrepot = dc.getEntrepot();
        // Extraction des échelles et des origines pour plus de lisibilité
        float[] util = calculerUtil(dc, panelWidth, panelHeight);
        if (tourAAfficher != null) {
            util = calculerUtilTour(panelWidth, panelHeight, tourAAfficher);
        }

        //System.out.println((service.getCatalogueTours().toString()));

        //service.setCarte(this);

        dessinerCarte();

        ArrayList<Intersection> listeInterPourLivrer = service.creerIntersectionsPourLivrer();

        ArrayList<Livraison> livraisons = new ArrayList<Livraison>();
        long id = 0;
        for (Intersection l : listeInterPourLivrer) {
            livraisons.add(service.creerLivraison(id, l, Creneau.valueOf("HUIT_NEUF"), LocalTime.MIDNIGHT, LocalTime.MIDNIGHT));
            id++;
        }

        CatalogueTours cat = service.getCatalogueTours();
        nbTours = tourAAfficher.getLivreur().getId().intValue() + 2;
        listeCouleurs = createElementListe(nbTours);

        ArrayList<Livraison> livraisonsFor = new ArrayList<>();
        Tour tourCol;
        int couleur = tourAAfficher.getLivreur().getId().intValue() + 1;
        tourCol = tourAColorier(tourAAfficher, dc, entrepot);
        dessinerTour(tourCol, couleur, tourAAfficher.getLivreur());
        //System.out.println("couleur"+ couleur+ " tailleliste"+ listeCouleurs.size());
        listeCouleurs.get(couleur-1).getDots().clear();
        livraisonsFor = tourAAfficher.getLivraisons();
        for (Livraison liv : livraisonsFor) {
            if (liv.livree()){
                Circle dot = createNode(liv.getAdresse(), util, 0, String.valueOf(liv.getId()));
                listeCouleurs.get(couleur - 1).getDots().add(dot);
                this.getChildren().add(dot);
            }
        }



        /*
        this.getChildren().add(createNode(listeInterPourLivrer.get(0),util,0,"3"));
        this.getChildren().add(createNode(listeInterPourLivrer.get(1),util,0,"4"));
        this.getChildren().add(createNode(listeInterPourLivrer.get(2),util,0,"5"));
        */


        Circle entrepotLocation = createNode(entrepot, util, 3, "Entrepot");
        this.getChildren().add(entrepotLocation);


        for (int i = 1; i < 5; i++) {
            enleverLigne(String.valueOf(i));
            remettreLigne(String.valueOf(i));
        }
    }


    /**
     * Constructeur de Carte, Affichage du graph et des tours
     * @param cheminFich chemin vers le fichier de la carte
     * @param panelWidth Largeur du panneau
     * @param panelHeight Hauteur du panneau
     */
    public Carte(String cheminFich, Integer panelWidth, Integer panelHeight) {
        super();
        this.cheminFichier = cheminFich;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;


        // Create a sample graph
        DonneesCarte dc = service.creerDonneesCarte(cheminFichier);
        graph = dc.getCarte();
        entrepot = dc.getEntrepot();
        // Extraction des échelles et des origines pour plus de lisibilité
        float[] util = calculerUtil(dc, panelWidth, panelHeight);
        if (tourAAfficher != null) {
            //util=calculerUtilTour(panelWidth,panelHeight,tourAAfficher);
        }

        //System.out.println((service.getCatalogueTours().toString()));

        service.setCarte(this);

        dessinerCarte();

        ArrayList<Intersection> listeInterPourLivrer = service.creerIntersectionsPourLivrer();

        ArrayList<Livraison> livraisons = new ArrayList<Livraison>();
        long id = 0;
        for (Intersection l : listeInterPourLivrer) {
            livraisons.add(service.creerLivraison(id, l, Creneau.valueOf("HUIT_NEUF"), LocalTime.MIDNIGHT, LocalTime.MIDNIGHT));
            id++;
        }

        CatalogueTours cat = service.getCatalogueTours();
        nbTours = cat.getCatalogue().size();
        listeCouleurs = createElementListe(nbTours);

        ArrayList<Livraison> livraisonsFor = new ArrayList<>();
        Tour tourFor = null;
        Tour tourCol;
        if (cat.getCatalogue().size() > 0) {
            for (int i = 0; i < cat.getCatalogue().size(); i++) {
                tourFor = cat.getCatalogue().get(i);
                livraisonsFor = tourFor.getLivraisons();
                tourCol = tourAColorier(tourFor, dc, entrepot);
                dessinerTour(tourCol, i + 1, tourFor.getLivreur());
                listeCouleurs.get(i).getDots().clear();
                for (Livraison liv : livraisonsFor) {
                    if(liv.livree()){
                        Circle dot = createNode(liv.getAdresse(), util, 0, String.valueOf(liv.getId()));
                        listeCouleurs.get(i).getDots().add(dot);
                        this.getChildren().add(dot);
                    }
                }
            }
        }

        /*
        this.getChildren().add(createNode(listeInterPourLivrer.get(0),util,0,"3"));
        this.getChildren().add(createNode(listeInterPourLivrer.get(1),util,0,"4"));
        this.getChildren().add(createNode(listeInterPourLivrer.get(2),util,0,"5"));
        */

        Circle entrepotLocation = createNode(entrepot, util, 3, "Entrepot");
        this.getChildren().add(entrepotLocation);

        for (int i = 1; i < 5; i++) {
            enleverLigne(String.valueOf(i));
            remettreLigne(String.valueOf(i));
        }
    }

    /**
     * Calcule les latitudes et longitudes min et max d'un Tour
     * @param tour Tour dont on souhaite connaitre ces infos
     * @return Le tableau [minLat, minLong, maxLat, maxLong]
     */
    private float[] getMinMax(Tour tour) {
        // Initialiser les valeurs min et max avec les premières intersections du tour
        double minLat = tour.getTrajet().get(0).getCoordonnees().getLatitude();
        double maxLat = minLat;
        double minLong = tour.getTrajet().get(0).getCoordonnees().getLongitude();
        double maxLong = minLong;

        // Parcourir toutes les intersections du tour
        for (Intersection intersection : tour.getTrajet()) {
            double lat = intersection.getCoordonnees().getLatitude();
            double lon = intersection.getCoordonnees().getLongitude();

            // Mettre à jour les valeurs min et max si nécessaire
            if (lat < minLat) minLat = lat;
            if (lat > maxLat) maxLat = lat;
            if (lon < minLong) minLong = lon;
            if (lon > maxLong) maxLong = lon;
        }

        // Calculez les différences de lat et long
        double latDiff = maxLat - minLat;
        double longDiff = maxLong - minLong;

        // Prenez la plus grande différence pour la taille du carré
        double size = Math.max(latDiff, longDiff);

        // Ajustez les limites pour former un carré
        maxLat = minLat + size;
        maxLong = minLong + size;

        minLat = minLat - 0.002;
        minLong = minLong - 0.002;
        maxLat = maxLat + 0.002;
        maxLong = maxLong + 0.002;

        // Retourner le tableau de résultats
        return new float[]{(float) minLat, (float) minLong, (float) maxLat, (float) maxLong};
    }


    /**
     * Permet de calculer les échelles et origines de la carte pour redimensionner la carte dans le panneau
     * @param dc Données de la carte
     * @param panelWidth Largeur du panneau
     * @param panelHeight Hauteur du panneau
     * @return Le tableau [originX, originY, echelleX, echelleY]
     */
    private float[] calculerUtil(DonneesCarte dc, Integer panelWidth, Integer panelHeight) {
        float echelleX = dc.getEchelleX() * panelWidth;
        float echelleY = dc.getEchelleY() * panelHeight;
        float originX = dc.getOrigine()[0];
        float originY = dc.getOrigine()[1];
        util = new float[]{originX, originY, echelleX, echelleY};
        return util;
    }

    /**
     * Permet de calculer les échelles et origine lors de l'affichage sur le détail d'un tour
     * @param panelWidth Largeur du panneau
     * @param panelHeight Hauteur du panneau
     * @param tour Le tour dont on souhaite connaitre les échelles
     * @return Le tableau [originX, originY, echelleX, echelleY]
     */
    private float[] calculerUtilTour(Integer panelWidth, Integer panelHeight, Tour tour) {
        float[] floats = getMinMax(tour);
        float echelleX = 1 / (floats[2] - floats[0]) * panelWidth;
        float echelleY = 1 / (floats[3] - floats[1]) * panelHeight;
        float originX = floats[0];
        float originY = floats[1];
        util = new float[]{originX, originY, echelleX, echelleY};
        return util;
    }

    /**
     *
     * @param tour Tour a colorier
     * @param carte Données de la carte
     * @param entrepot Intersection de l'entrepor
     * @return Le tour à colorier
     */
    private Tour tourAColorier(Tour tour, DonneesCarte carte, Intersection entrepot) {
        Tour t = service.calculerTour(tour, 15.0, carte, entrepot);
        return t;
    }

    /**
     * Dessine le tour dans le panneau de la carte
     * @param tour Tour à afficher
     * @param couleur Couleur de l'affichage
     * @param livr Livreur associé au tour
     */
    void dessinerTour(Tour tour, int id, Livreur livr) {
        ArrayList<Intersection> cheminTour = tour.getTrajet();
        Intersection destination = cheminTour.get(0);

        // Ajout des Livraisons entre les points arrivée et départ
        for (Map.Entry<Intersection, Map<Intersection, Float>> entry : graph.entrySet()) {
            Intersection sourceNode = entry.getKey();
            //test car sinon beug
            if (sourceNode != null) {
                Map<Intersection, Float> edges = entry.getValue();
                for (Map.Entry<Intersection, Float> edge : edges.entrySet()) {
                    Intersection targetNode = edge.getKey();
                    float length = edge.getValue();
                    if (cheminTour.contains(targetNode) && cheminTour.contains(sourceNode)) {

                        Circle depart = createNode(sourceNode, util, id, "");
                        Circle arrivee = createNode(targetNode, util, id, "");


                        Line edgeToAdd = createEdge(depart, arrivee, length, id);
                        edgeToAdd.setStrokeWidth(2);
                        int index = id - 1;
                        //System.out.println("hello index = "+ index);
                        if (index >= 0 && index < listeCouleurs.size()) {
                            //  System.out.println("hello id = "+ id);
                            listeCouleurs.get(index).getLines().add(edgeToAdd);
                        }


                        //Ajout d'un rectangle pour que la detection de la souris soit plus grande
                        Rectangle rect = new Rectangle();
                        rect.setX(Math.min(depart.getCenterX(), arrivee.getCenterX()) - 10);
                        rect.setY(Math.min(depart.getCenterY(), arrivee.getCenterY()) - 10);
                        rect.setWidth(Math.abs(depart.getCenterX() - arrivee.getCenterX()) + 10);
                        rect.setHeight(Math.abs(depart.getCenterY() - arrivee.getCenterY()) + 10);
                        rect.setFill(Color.TRANSPARENT);


                        Tooltip tooltip = new Tooltip();
                        //System.out.println("hello listecoleur = "+ listeCouleurs.size());
                        if (index >= 0 && index < listeCouleurs.size()) {

                            rect.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                                if (listeCouleurs.get(index).getEtat()) {
                                    for (Line line : listeCouleurs.get(index).getLines()) {
                                        line.setStrokeWidth(5);
                                    }
                                    tooltip.setText("Livreur " + livr.getId() + " (" + livr.getNom() + ")");
                                    tooltip.show(rect, event.getScreenX(), event.getScreenY());
                                }
                            });

                            rect.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
                                tooltip.setX(event.getScreenX());
                                tooltip.setY(event.getScreenY());
                            });

                            rect.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
                                for (Line line : listeCouleurs.get(index).getLines()) {
                                    line.setStrokeWidth(1);
                                }
                                tooltip.hide();
                            });
                        }


                        this.getChildren().addAll(edgeToAdd, rect);

                        //si l'intersection ne fait pas partie du tour
                    } else {

                    }
                }
            }
        }
    }

    /**
     * Dessine la carte dans le panneau
     */
    void dessinerCarte() {
        for (Map.Entry<Intersection, Map<Intersection, Float>> entry : graph.entrySet()) {
            Intersection sourceNode = entry.getKey();
            //test car sinon beug
            if (sourceNode != null) {
                Map<Intersection, Float> edges = entry.getValue();
                for (Map.Entry<Intersection, Float> edge : edges.entrySet()) {
                    Intersection targetNode = edge.getKey();
                    float length = edge.getValue();

                    // Creation du point de départ
                    Circle depart = createNode(sourceNode, util, null, "");
                    // Creation du point d'arrivée
                    Circle arrivee = createNode(targetNode, util, null, "");

                    // Creation de l'arrête reliant les deux
                    Line edgeToAdd = createEdge(depart, arrivee, length, 0);
                    edgeToAdd.setStrokeWidth(1);
                    this.getChildren().add(edgeToAdd);

                }
            }
        }
    }

    /**
     * Crée un point sur la carte
     * @param inter intersection sur laquelle crée le point
     * @param util Les échelles et origines
     * @param color Couleur dur point
     * @param description Description affiché lors d'un hover du point
     * @return Point créé
     */
    private Circle createNode(Intersection inter, float[] util, Integer color, String description) {
        Circle node = new Circle(); // Circle with radius 10
        //util[0] = originX
        //util[1] = originY
        //util[2] = echelleX
        //util[3] = echelleY

        float originX = util[0];
        float originY = util[1];
        float echelleX = util[2];
        float echelleY = util[3];
        double x = (inter.getCoordonnees().getLatitude() - originX) * echelleX;
        double y = (inter.getCoordonnees().getLongitude() - originY) * echelleY;
        node.setCenterX(x);
        node.setCenterY(y);
        if (color == null || color == 0) {
            node.setStyle("-fx-fill: black;");
            node.setRadius(8);
        } else if (color == 3) {
            node.setStyle("-fx-fill: green;");
            node.setRadius(8);
        } else if (color == 1) {
            node.setStyle("-fx-fill: transparent; -fx-stroke:transparent");
            node.setRadius(8);
        } else if (color == 2) {
            node.setStyle("-fx-fill: transparent; -fx-stroke:transparent");
            node.setRadius(8);
        }

        Tooltip tooltip = new Tooltip();
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        node.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            int index = color - 1;
            if (index >= 0 && index < listeCouleurs.size()) {
                if (listeCouleurs.get(index).getEtat()) {
                    tooltip.setText(description);
                    tooltip.show(node, event.getScreenX(), event.getScreenY());
                    delay.playFromStart();
                }
            } else {
                tooltip.setText(description);
                tooltip.show(node, event.getScreenX(), event.getScreenY());
                delay.playFromStart();
            }


        });
        delay.setOnFinished(event -> tooltip.hide());
        node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            delay.playFromStart();
        });

        return node;
    }

    /**
     * Crée sur la carte un arc entre deux points
     * @param source Origine de l'arc
     * @param target Destination de l'arc
     * @param color Couleur de l'arc
     * @return L'arc
     */
    private Line createEdge(Circle source, Circle target, float length, Integer color) {
        Line edge = new Line();

        edge.startXProperty().bind(source.centerXProperty());
        edge.startYProperty().bind(source.centerYProperty());

        edge.endXProperty().bind(target.centerXProperty());
        edge.endYProperty().bind(target.centerYProperty());
        if (color == 1) {
            edge.setStyle("-fx-stroke: red; ");
        } else if (color == 2) {
            edge.setStyle("-fx-stroke: blue; ");
        } else if (color == 0) {
            edge.setStyle("-fx-stroke: black;");
        }
        return edge;
    }

    /**
     * Permet de cacher un tour de l'affichage sur la carte
     * @param numTour Id du tour à cacher
     */
    void enleverLigne(String numTour) {
        int index = Integer.parseInt(numTour) - 1;
        if (index >= 0 && index < listeCouleurs.size()) {
            for (Line l : listeCouleurs.get(index).getLines()) {
                l.setStroke(Color.BLACK);
                l.setStrokeWidth(1);
            }
            //System.out.println("numtour : "+numTour+" taillegetdots :"+listeCouleurs.get(index).getDots().size());
            for (Circle d : listeCouleurs.get(index).getDots()) {
                //d.setStroke(Color.GREEN);
                d.setVisible(false);
            }
            listeCouleurs.get(index).setEtat(false);
        }
    }

    /**
     * Permet d'afficher un tour caché sur la carte
     * @param numTour Numéro du tour à ré-afficher
     */
    void remettreLigne(String numTour) {
        int index = Integer.parseInt(numTour) - 1;
        if (index >= 0 && index < listeCouleurs.size()) {
            Color color = switch (index) {
                case 0 -> Color.RED;
                case 1 -> Color.BLUE;
                case 2 -> Color.GREEN;
                case 3 -> Color.YELLOW;
                case 4 -> Color.ORANGE;
                default -> throw new IndexOutOfBoundsException("Invalid index: " + index);
            };
            for (Line l : listeCouleurs.get(index).getLines()) {
                l.setStroke(color);
            }
            for (Circle d : listeCouleurs.get(index).getDots()) {
                d.setStroke(color);
                d.setVisible(true);
            }
            listeCouleurs.get(index).setEtat(true);
        }
    }

    /**
     * Crée les élément de la liste qui permettent de gérer l'affichage et le cachage des tour
     * @param nbTours Nb de tours totaux
     * @return Liste des elements
     */
    List<ElementListe> createElementListe(int nbTours) {
        List<ElementListe> listeElement = new ArrayList<>();
        for (int i = 0; i < nbTours; i++) {
            List<Line> lines = new ArrayList<>();
            List<Circle> dots = new ArrayList<>();
            boolean state = true;
            ElementListe elementListe = new ElementListe(lines, dots, state);
            listeElement.add(elementListe);
        }
        return listeElement;
    }


    void ouvrirDetails(Long id) {
        Tour tour = Service.getInstance().getCatalogueTours().getTourById(id);
        DetailsTour fenetreDetails = new DetailsTour(this.cheminFichier, tour, 800, 550);
        fenetreDetails.ouvrirFenetre();
    }

    int getNbTours() {
        return nbTours;
    }

    void surlignerLigne(int ligne) {
        if (listeCouleurs.get(ligne).getEtat()) {
            for (Line l : listeCouleurs.get(ligne).getLines()) {
                l.setStrokeWidth(4);
            }
        }
    }

    void amincirLigne(int ligne) {
        if (listeCouleurs.get(ligne).getEtat()) {
            for (Line l : listeCouleurs.get(ligne).getLines()) {
                l.setStrokeWidth(1);
            }
        }
    }
}

class ElementListe {
    List<Line> lines;
    List<Circle> dots;
    boolean etat;

    /**
     * La classe ElementsList contient les éléments pour l'affichage graphique d'un tour sur la carte
     * @param lines Arcs du tour
     * @param dots Points du tour
     * @param etat Tour affiché ou caché
     */
    public ElementListe(List<Line> lines, List<Circle> dots, boolean etat) {
        this.lines = lines;
        this.dots = dots;
        this.etat = etat;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Circle> getDots() {
        return dots;
    }

    public void setDot(List<Circle> dots) {
        this.dots = dots;
    }

    public boolean getEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

}
