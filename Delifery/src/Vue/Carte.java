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

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.*;

public class Carte extends Pane {

    //nb de tours au total à afficher
    int nbTours=2;

    List<ElementListe> listeCouleurs=new ArrayList<>();

    Intersection entrepot;
    float[] util;
    private Map<Intersection, Map<Intersection, Float>> graph;
    private String cheminFichier;
    Service service = Service.getInstance();

    public Carte(String cheminFich, Integer panelWidth, Integer panelHeight) {
        super();
        this.cheminFichier = cheminFich;
        // Create a sample graph
        DonneesCarte dc = createSampleGraph();
        entrepot = dc.getEntrepot();
        // Extraction des échelles et des origines pour plus de lisibilité

        float [] util =calculerUtil(dc, panelWidth, panelHeight);


        listeCouleurs=createElementListe(nbTours);


        dessinerCarte();

        ArrayList<Intersection> listeInterPourLivrer= service.creerIntersectionsPourLivrer();

        ArrayList<Livraison> livraisons = new ArrayList<Livraison>();
        long id  = 0;
        for(Intersection l : listeInterPourLivrer){
            livraisons.add(service.creerLivraison(id, l, Creneau.valueOf("HUIT_NEUF"),LocalTime.MIDNIGHT,LocalTime.MIDNIGHT));
            id++;
        }




        this.getChildren().add(createNode(listeInterPourLivrer.get(0),util,0,"3"));
        this.getChildren().add(createNode(listeInterPourLivrer.get(1),util,0,"4"));
        this.getChildren().add(createNode(listeInterPourLivrer.get(2),util,0,"5"));


        ArrayList<Livraison> livraisons1 = new ArrayList<>(livraisons);
        livraisons1.remove(0);
        livraisons1.remove(1);
        ArrayList<Livraison> livraisons2 = new ArrayList<>(livraisons);
        livraisons2.remove(3);
        livraisons2.remove(2);

        //livreur
        Livreur livreur1 = service.initialisationLivreur("Sophie");
        Livreur livreur2 = service.initialisationLivreur("Maria");
        ArrayList<Intersection> cheminTotal = new ArrayList<>();



        Tour tour1=service.creerTour(livraisons1 ,cheminTotal,livreur1);
        tour1 = tourAColorier(tour1, dc, entrepot);



        ArrayList<Intersection> cheminTotal2 = new ArrayList<>();

        Tour tour2=service.creerTour(livraisons2 ,cheminTotal2, livreur2);
        tour2 =tourAColorier(tour2,dc, entrepot);
        System.out.println(tour2.toString());

        Circle entrepotLocation = createNode(entrepot, util, 3,"Entrepot");
        this.getChildren().add(entrepotLocation);

        //dessinerTour(tour1, 1);
        //dessinerTour(tour2, 2);





        //section2Tour1.remove(0);
        //section1Tour1.remove(0);




       /* enleverLigne("1");
        enleverLigne("2");
        remettreLigne("1");
        remettreLigne("2");*/


    }

    private DonneesCarte createSampleGraph() {
        Service s = new Service();
        DonneesCarte dc = s.creerDonneesCarte(cheminFichier);
        graph = dc.getCarte();


        return dc;
    }

    private float[] calculerUtil(DonneesCarte dc, Integer panelWidth, Integer panelHeight){
        float echelleX = dc.getEchelleX() * panelWidth;
        float echelleY = dc.getEchelleY() * panelHeight;
        float originX = dc.getOrigine()[0];
        float originY = dc.getOrigine()[1];
        util = new float[]{originX, originY, echelleX, echelleY};
        return util ;
    }



    private ArrayList<Intersection> testCalculChemin(Intersection dest1,Intersection dest2) {

        List<Intersection> chemin2 = Calculs.dijkstra(dest1,dest2,graph);

        ArrayList<Intersection> monChemin = new ArrayList<Intersection>(chemin2);

        //recuperer les sections de mon chemin qui ne sont pas des livraisons
        ArrayList<Intersection> sectionAColorier = new ArrayList<Intersection>();

        sectionAColorier.add(dest1);
        sectionAColorier.add(dest2);

        for (Intersection i : monChemin) {
            sectionAColorier.add(i);
        }

        return sectionAColorier;
    }

    private Tour tourAColorier(Tour tour,  DonneesCarte carte, Intersection entrepot){
        Tour t = service.calculerTour(tour, 15.0, carte, entrepot);
        return t;
    }

    void dessinerTour(Tour tour, int id) {
        ArrayList<Intersection> cheminTour = tour.getTrajet();
        Intersection destination =  cheminTour.get(0);
        // Ajout des points de destination
        for (Intersection inter : cheminTour) {
            if (inter != null) {
                if (inter == destination) {
                    // Ajout du point
                    Circle arrivee = createNode(inter, util, id,destination.toString());
                    int index = id - 1;
                    if (index >= 0 && index < listeCouleurs.size()) {
                        listeCouleurs.get(index).setDot(arrivee);
                        this.getChildren().add(listeCouleurs.get(index).getDot());
                    }
                }
            }
        }

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

                        Circle depart = createNode(sourceNode, util, id,"");
                        Circle arrivee = createNode(targetNode, util, id,"");


                        Line edgeToAdd = createEdge(depart, arrivee, length, id);
                        edgeToAdd.setStrokeWidth(2);
                        int index = id - 1;
                        System.out.println("hello index = "+ index);
                        if (index >= 0 && index < listeCouleurs.size()) {
                            System.out.println("hello id = "+ id);
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
                        System.out.println("hello listecoleur = "+ listeCouleurs.size());
                        if (index >= 0 && index < listeCouleurs.size()) {

                            rect.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                                if (listeCouleurs.get(index).getEtat()){
                                    for (Line line : listeCouleurs.get(index).getLines()) {
                                        line.setStrokeWidth(5);
                                    }
                                    tooltip.setText("Tour " + id);
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
                    Circle depart = createNode(sourceNode, util, null,"");
                    // Creation du point d'arrivée
                    Circle arrivee = createNode(targetNode, util, null,"");

                    // Creation de l'arrête reliant les deux
                    Line edgeToAdd = createEdge(depart, arrivee, length, 0);
                    edgeToAdd.setStrokeWidth(1);
                    this.getChildren().add(edgeToAdd);

                }
            }
        }
    }

    private Circle createNode(Intersection inter, float[] util, Integer color,String description) {
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
                if(listeCouleurs.get(index).getEtat()){
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

    void enleverLigne(String numTour) {
        int index = Integer.parseInt(numTour) - 1;
        if (index >= 0 && index < listeCouleurs.size()) {
            for (Line l : listeCouleurs.get(index).getLines()) {
                l.setStroke(Color.BLACK);
                l.setStrokeWidth(1);
            }
            listeCouleurs.get(index).getDot().setVisible(false);
            listeCouleurs.get(index).setEtat(false);
        }
    }

    /*void remettreLigne(String numTour) {
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
            listeCouleurs.get(index).getDot().setFill(color);
            listeCouleurs.get(index).getDot().setStroke(color);
            listeCouleurs.get(index).getDot().setVisible(true);
            listeCouleurs.get(index).setEtat(true);
        }
    }*/

    List<ElementListe> createElementListe(int nbTours){
        List<ElementListe> listeElement = new ArrayList<>();
        for (int i=0;i<nbTours;i++){
            List<Line> lines = new ArrayList<>();
            Circle dot =new Circle();
            boolean state =true;
            ElementListe elementListe=new ElementListe(lines,dot ,state);
            listeElement.add(elementListe);
        }
        return listeElement;
    }


    void ouvrirDetails(Long id){
        Tour tour = Service.getInstance().getCatalogueTours().getTourById(id);
        DetailsTour fenetreDetails = new DetailsTour(this.cheminFichier, tour, 800, 550);
        fenetreDetails.ouvrirFenetre();
    }

    int getNbTours(){
        return nbTours;
    }

}

class ElementListe {
    List<Line> lines;
    Circle dot;
    boolean etat;

    public ElementListe(List<Line> lines, Circle circle, boolean number) {
        this.lines = lines;
        this.dot = circle;
        this.etat = number;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public Circle getDot() {
        return dot;
    }

    public void setDot(Circle dot) {
        this.dot = dot;
    }

    public boolean getEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }
}
