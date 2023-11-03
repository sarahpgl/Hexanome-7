package Vue;

import Donnees.DonneesCarte;
import Donnees.Intersection;
import Service.Service;
import Util.Coordonnees;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Carte extends Pane {

    List<Line> redLines=new ArrayList<>();
    List<Line> blueLines=new ArrayList<>();

    Intersection entrepot;

    private Map<Intersection, Map<Intersection, Float>> graph;
    private String cheminFichier;

    float[]util;

    public Carte(String cheminFich, Integer panelHeight, Integer panelWidth) {
        super();
        this.cheminFichier = cheminFich;
        // Create a sample graph
        DonneesCarte dc = createSampleGraph();
        entrepot=dc.getEntrepot();
        // Extraction des échelles et des origines pour plus de lisibilité
        float echelleX = dc.getEchelleX() * panelHeight;
        float echelleY = dc.getEchelleY() * panelWidth;
        float originX = dc.getOrigine()[0];
        float originY = dc.getOrigine()[1];
        util = new float[]{originX, originY, echelleX, echelleY};


        // Création du point entrepôt
        Circle entrepotLocation = createNode(entrepot, util, 0);
        this.getChildren().add(entrepotLocation);

        dessinerCarte();


        //test de recupere un tour
        Intersection destination3 = new Intersection(new BigInteger("27362284"), new Coordonnees(45.728672, 4.876898));
        Intersection destination4 = new Intersection(new BigInteger("1345284783"), new Coordonnees(45.752106, 4.8660784));

        ArrayList<Intersection> sectionAColorier1 = testCalculChemin(destination3);
        dessinerTour(sectionAColorier1,1);
        ArrayList<Intersection> sectionAColorier2 = testCalculChemin(destination4);
        dessinerTour(sectionAColorier2,2);

        sectionAColorier1.remove(0);
        sectionAColorier2.remove(0);




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
            //if(i != destination3 && i != Entrepot){
            sectionAColorier.add(i);
            //}
        }

        return sectionAColorier;
    }

    void dessinerTour(ArrayList<Intersection> cheminTour,int id){
        // Ajout des points de départ et  (en théorie c'est le meme : l'entrepot)
        for (Intersection inter : graph.keySet()) {
            if (inter != null) {
                if (inter == entrepot || inter ==cheminTour.getFirst()) {
                    // Ajout du point
                    Circle entrepot1 = createNode(inter, util, 0);
                    this.getChildren().add(entrepot1);

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
                    //si l'intersection fait partie du tour

                    if (cheminTour.contains(targetNode) && cheminTour.contains(sourceNode)) {
                        // Creation du point de départ
                        Circle depart = createNode(sourceNode, util, id);
                        // Creation du point d'arrivée
                        Circle arrivee = createNode(targetNode, util, id);
                        // Creation de l'arrête reliant les deux
                        Line edgeToAdd = createEdge(depart, arrivee, length, id);
                        if(id==1){
                            redLines.add(edgeToAdd);
                        }
                        if(id==2){
                            blueLines.add(edgeToAdd);
                        }

                        //Ajout d'un rectangle pour que la detection de la souris soit plus grande
                        Rectangle rect = new Rectangle();
                        rect.setX(Math.min(depart.getCenterX(), arrivee.getCenterX())-10);
                        rect.setY(Math.min(depart.getCenterY(), arrivee.getCenterY())-10);
                        rect.setWidth(Math.abs(depart.getCenterX() - arrivee.getCenterX())+10);
                        rect.setHeight(Math.abs(depart.getCenterY() - arrivee.getCenterY())+10);
                        rect.setFill(Color.TRANSPARENT);

                        if(id==1){
                            rect.setOnMouseEntered(event -> {
                                for (Line line : redLines) {
                                    line.setStrokeWidth(5);
                                }
                            });
                            rect.setOnMouseExited(event -> {
                                for (Line line : redLines) {
                                    line.setStrokeWidth(1);
                                }
                            });
                        }
                        if(id==2){
                            rect.setOnMouseEntered(event -> {
                                for (Line line : blueLines) {
                                    line.setStrokeWidth(5);
                                }
                            });
                            rect.setOnMouseExited(event -> {
                                for (Line line : blueLines) {
                                    line.setStrokeWidth(1);
                                }
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
                        Circle depart = createNode(sourceNode, util, null);

                        // Creation du point d'arrivée
                        Circle arrivee = createNode(targetNode, util, null);

                        // Creation de l'arrête reliant les deux
                        Line edgeToAdd = createEdge(depart, arrivee, length, 0);
                        edgeToAdd.setStrokeWidth(1);
                        this.getChildren().add(edgeToAdd);

                }
            }
        }
    }

    private Circle createNode(Intersection inter, float[] util, Integer color) {
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
        if (color == null) {
            node.setStyle("-fx-fill: black;");
            node.setRadius(1);
        }
        //pour colorer l'entrepôt en vert
        else if (color == 3) {
            node.setStyle("-fx-fill: green;");
            node.setRadius(3);
        } else if (color == 1) {
            node.setStyle("-fx-fill: red;");
            node.setRadius(3);
        } else if (color == 0) {
            node.setStyle("-fx-fill: black;");
            node.setRadius(3);
        }
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
        }
        if (color == 2) {
            edge.setStyle("-fx-stroke: blue; ");
        }else {
            edge.setStyle("-fx-stroke: black;");
        }
        return edge;
    }

    void enleverLigne(String numTour){
        if (Objects.equals(numTour, "1")){
            for (Line l: redLines){
                l.setStroke(Color.BLACK);
            }
        }
        if (Objects.equals(numTour, "2")){
            for (Line l: blueLines){
                l.setStroke(Color.BLACK);
            }
        }
    }
    void remettreLigne(String numTour){
        if (Objects.equals(numTour, "1")){
            for (Line l: redLines){
                l.setStroke(Color.RED);
            }
        }
        if (Objects.equals(numTour, "2")){
            for (Line l: blueLines){
                l.setStroke(Color.BLUE);
            }
        }
    }

}
