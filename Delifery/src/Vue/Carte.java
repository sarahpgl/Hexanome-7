package Vue;

import Util.Coordonnees;
import Service.Service;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.*;
import Donnees.Intersection;
import Donnees.DonneesCarte;
import Util.Coordonnees;

public class Carte extends Pane {
    private Map<Intersection, Map<Intersection, Float>> graph;
    private String chemin;

    public Carte(String chemin, Integer panelHeight, Integer panelWidth) {
        super();

        this.chemin = chemin;
        // Create a sample graph
        DonneesCarte dc = createSampleGraph();

        // Extraction des échelles et des origines pour plus de lisibilité
        float echelleX = dc.getEchelleX()*panelHeight;
        float echelleY = dc.getEchelleY()*panelWidth;
        float originX = dc.getOrigine()[0];
        float originY = dc.getOrigine()[1];

        // Création du point entrepôt
        Intersection entrepot = dc.getEntrepot();
        Circle entrepotLocation = createNode((entrepot.getCoordonnees().getLatitude()-originX)*echelleX,(entrepot.getCoordonnees().getLongitude()-originY)*echelleY,1);
        this.getChildren().add(entrepotLocation);

        //test de recupere un tour et de le colorier en rouge sur la map
        Service service = new Service();
        Intersection Entrepot = new Intersection(new BigInteger("25303831"),new Coordonnees(45.74979,4.87572));
        Intersection destination1 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214,4.875591));
        Intersection destination2 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969,4.873468));
        Intersection destination3 = new Intersection(new BigInteger("27362284"), new Coordonnees(45.728672,4.876898));
        List<Intersection> chemin2 = service.dijkstra(Entrepot,destination3,graph);

        ArrayList<Intersection> monChemin = new ArrayList<Intersection>(chemin2);

        //recuperer les sections de mon chemin qui ne sont pas des livraisons
        ArrayList<Intersection> sectionAColorier = new ArrayList<Intersection>();
        for(Intersection i : monChemin){
            if(i != destination3 && i != Entrepot){
                sectionAColorier.add(i);
            }
        }

        // Add nodes to the container
        for (Intersection inter : graph.keySet()) {
            if (inter != null){
                if(inter == Entrepot || inter == destination3) {
                    // Calcul des coordonnées x et y
                    double x = (inter.getCoordonnees().getLatitude() - originX) * echelleX;
                    double y = (inter.getCoordonnees().getLongitude() - originY) * echelleY;

                    // Ajout du point
                    Circle point = createNode(x, y, 2);
                    this.getChildren().add(point);
                }else{
                    // Calcul des coordonnées x et y
                    double x = (inter.getCoordonnees().getLatitude() - originX) * echelleX;
                    double y = (inter.getCoordonnees().getLongitude() - originY) * echelleY;

                    // Ajout du point
                    Circle point = createNode(x, y, null);
                    this.getChildren().add(point);
                }
             }
        }

        // Add edges to the container
        for (Map.Entry<Intersection, Map<Intersection, Float>> entry : graph.entrySet()) {
            Intersection sourceNode = entry.getKey();
            //test car sinon beug
            if (sourceNode!= null) {
                Map<Intersection, Float> edges = entry.getValue();
                for (Map.Entry<Intersection, Float> edge : edges.entrySet()) {

                    Intersection targetNode = edge.getKey();
                    float length = edge.getValue();
                    if(sectionAColorier.contains(targetNode) && sectionAColorier.contains(sourceNode)) {
                        // Creation du point de départ
                        double x1 = (sourceNode.getCoordonnees().getLatitude() - originX) * echelleX;
                        double y1 = (sourceNode.getCoordonnees().getLongitude() - originY) * echelleY;
                        Circle point1 = createNode(x1, y1, 2);

                        // Creation du point d'arrivée
                        double x2 = (targetNode.getCoordonnees().getLatitude() - originX) * echelleX;
                        double y2 = (targetNode.getCoordonnees().getLongitude() - originY) * echelleY;
                        Circle point2 = createNode(x2, y2, 2);

                        // Creation de l'arête reliant les deux
                        Line edgeToAdd = createEdge(point1, point2, length, 2);
                        this.getChildren().add(edgeToAdd);
                    }else{
                        // Creation du point de départ
                        double x1 = (sourceNode.getCoordonnees().getLatitude() - originX) * echelleX;
                        double y1 = (sourceNode.getCoordonnees().getLongitude() - originY) * echelleY;
                        Circle point1 = createNode(x1, y1, null);

                        // Creation du point d'arrivée
                        double x2 = (targetNode.getCoordonnees().getLatitude() - originX) * echelleX;
                        double y2 = (targetNode.getCoordonnees().getLongitude() - originY) * echelleY;
                        Circle point2 = createNode(x2, y2, null);

                        // Creation de l'arête reliant les deux
                        Line edgeToAdd = createEdge(point1, point2, length, 0);
                        this.getChildren().add(edgeToAdd);
                    }
                }
            }
        }

    }

    private DonneesCarte createSampleGraph() {
        Service s = new Service();
        DonneesCarte dc = s.creerDonneesCarte(chemin);
        graph = dc.getCarte();
        Intersection entrepot = dc.getEntrepot();

        return dc;
    }

    private Circle createNode(double x, double y, Integer color) {
        Circle node = new Circle(); // Circle with radius 10
        node.setCenterX(x);
        node.setCenterY(y);
        if (color== null){
            node.setStyle("-fx-fill: black;");
            node.setRadius(3);
        }
        //pour colorer l'entrepôt en vert
        else if (color ==1){
            node.setStyle("-fx-fill: green;");
            node.setRadius(6);
        }

        else if(color == 2){
            node.setStyle("-fx-fill: red;");
            node.setRadius(4);
        }
        return node;
    }

    private Line createEdge(Circle source, Circle target, float length, Integer color) {
        Line edge = new Line();

        edge.startXProperty().bind(source.centerXProperty());
        edge.startYProperty().bind(source.centerYProperty());

        edge.endXProperty().bind(target.centerXProperty());
        edge.endYProperty().bind(target.centerYProperty());
        if(color == 2){
            edge.setStyle("-fx-stroke: red; -fx-stroke-width: 2;");
        }else{
            edge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        }
        return edge;
    }

}
