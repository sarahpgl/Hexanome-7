package Vue;

import Donnees.DonneesCarte;
import Donnees.Intersection;
import Service.Service;
import Util.Coordonnees;
import javafx.animation.PauseTransition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Carte extends Pane {

    List<Line> redLines = new ArrayList<>();
    Circle redDot;
    boolean redActivated=true;
    List<Line> blueLines = new ArrayList<>();
    Circle blueDot;
    boolean blueActivated=true;
    Intersection entrepot;
    float[] util;
    private Map<Intersection, Map<Intersection, Float>> graph;
    private String cheminFichier;

    public Carte(String cheminFich, Integer panelHeight, Integer panelWidth) {
        super();
        this.cheminFichier = cheminFich;
        // Create a sample graph
        DonneesCarte dc = createSampleGraph();
        entrepot = dc.getEntrepot();
        // Extraction des échelles et des origines pour plus de lisibilité
        float echelleX = dc.getEchelleX() * panelHeight;
        float echelleY = dc.getEchelleY() * panelWidth;
        float originX = dc.getOrigine()[0];
        float originY = dc.getOrigine()[1];
        util = new float[]{originX, originY, echelleX, echelleY};




        dessinerCarte();


        //test de recupere un tour
        Intersection destination3 = new Intersection(new BigInteger("27362284"), new Coordonnees(45.728672, 4.876898));
        Intersection destination4 = new Intersection(new BigInteger("1345284783"), new Coordonnees(45.752106, 4.8660784));

        ArrayList<Intersection> sectionAColorier2 = testCalculChemin(destination4);
        dessinerTour(sectionAColorier2, 2, destination4);

        ArrayList<Intersection> sectionAColorier1 = testCalculChemin(destination3);
        dessinerTour(sectionAColorier1, 1, destination3);


        sectionAColorier1.remove(0);
        sectionAColorier2.remove(0);

        Circle entrepotLocation = createNode(entrepot, util, 3,"Entrepot");
        this.getChildren().add(entrepotLocation);

        remettreLigne("1");
        remettreLigne("2");

    }

    private DonneesCarte createSampleGraph() {
        Service s = new Service();
        DonneesCarte dc = s.creerDonneesCarte(cheminFichier);
        graph = dc.getCarte();


        return dc;
    }

    private ArrayList<Intersection> testCalculChemin(Intersection dest) {
        Service service = new Service();
        Intersection Entrepot = new Intersection(new BigInteger("25303831"), new Coordonnees(45.74979, 4.87572));

        List<Intersection> chemin = service.dijkstra(Entrepot, dest, graph);

        ArrayList<Intersection> monChemin = new ArrayList<Intersection>(chemin);

        //recuperer les sections de mon chemin qui ne sont pas des livraisons
        ArrayList<Intersection> sectionAColorier = new ArrayList<Intersection>();

        sectionAColorier.add(dest);

        for (Intersection i : monChemin) {
            sectionAColorier.add(i);
        }

        return sectionAColorier;
    }

    void dessinerTour(ArrayList<Intersection> cheminTour, int id, Intersection destination) {

        // Ajout des points de destination
        for (Intersection inter : cheminTour) {
            if (inter != null) {
                if (inter == destination) {
                    // Ajout du point
                    Circle arrivee = createNode(inter, util, id,destination.toString());
                    if(id==1){
                        redDot=arrivee;
                        this.getChildren().add(redDot);
                    }
                    if(id==2){
                        blueDot=arrivee;
                        this.getChildren().add(blueDot);
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
                        if (id == 1) {
                            redLines.add(edgeToAdd);
                        }
                        if (id == 2) {
                            blueLines.add(edgeToAdd);
                        }

                        //Ajout d'un rectangle pour que la detection de la souris soit plus grande
                        Rectangle rect = new Rectangle();
                        rect.setX(Math.min(depart.getCenterX(), arrivee.getCenterX()) - 10);
                        rect.setY(Math.min(depart.getCenterY(), arrivee.getCenterY()) - 10);
                        rect.setWidth(Math.abs(depart.getCenterX() - arrivee.getCenterX()) + 10);
                        rect.setHeight(Math.abs(depart.getCenterY() - arrivee.getCenterY()) + 10);
                        rect.setFill(Color.TRANSPARENT);

                        Tooltip tooltip = new Tooltip();
                        if (id == 1) {
                            rect.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                                if (redActivated){
                                    for (Line line : redLines) {
                                        line.setStrokeWidth(5);
                                    }
                                    tooltip.setText("Tour " + id + "\nDestination : " + destination.toString());
                                    tooltip.show(rect, event.getScreenX(), event.getScreenY());
                                }
                            });

                            rect.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
                                tooltip.setX(event.getScreenX());
                                tooltip.setY(event.getScreenY());
                            });

                            rect.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
                                for (Line line : redLines) {
                                    line.setStrokeWidth(2);
                                }
                                tooltip.hide();
                            });
                        }
                        if (id == 2) {

                            rect.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                                if (blueActivated) {
                                    for (Line line : blueLines) {
                                        line.setStrokeWidth(5);
                                    }
                                    tooltip.setText("Tour " + id + "\nDestination : " + destination.toString());
                                    tooltip.show(rect, event.getScreenX(), event.getScreenY());
                                }
                            });

                            rect.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
                                tooltip.setX(event.getScreenX());
                                tooltip.setY(event.getScreenY());
                            });

                            rect.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
                                for (Line line : blueLines) {
                                    line.setStrokeWidth(2);
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
            node.setStyle("-fx-fill: transparent;");
            node.setRadius(8);
        } else if (color == 2) {
            node.setStyle("-fx-fill: transparent;");
            node.setRadius(8);
        }

        Tooltip tooltip = new Tooltip();
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        node.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            if(color==1){
                if(redActivated){
                    tooltip.setText(description);
                    tooltip.show(node, event.getScreenX(), event.getScreenY());
                    delay.playFromStart();
                }
            } else if(color==2){
                if(blueActivated){
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
        if (Objects.equals(numTour, "1")) {
            for (Line l : redLines) {
                l.setStroke(Color.BLACK);
                l.setStrokeWidth(1);
            }
            redDot.setVisible(false);
            redActivated=false;
        }
        if (Objects.equals(numTour, "2")) {
            for (Line l : blueLines) {
                l.setStroke(Color.BLACK);
                l.setStrokeWidth(1);
            }
            blueDot.setVisible(false);
            blueActivated=false;
        }
    }

    void remettreLigne(String numTour) {
        if (Objects.equals(numTour, "1")) {
            for (Line l : redLines) {
                l.setStroke(Color.RED);
                l.setStrokeWidth(3);
            }
            redDot.setFill(Color.RED);
            redDot.setStroke(Color.RED);
            redDot.setVisible(true);
            redActivated=true;
        }
        if (Objects.equals(numTour, "2")) {
            for (Line l : blueLines) {
                l.setStroke(Color.BLUE);
                l.setStrokeWidth(3);
            }
            blueDot.setStroke(Color.BLUE);
            blueDot.setFill(Color.BLUE);
            blueDot.setVisible(true);
            blueActivated=true;
        }
    }

}
