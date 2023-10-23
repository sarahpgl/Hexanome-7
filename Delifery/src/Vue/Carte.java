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

import java.util.HashMap;
import java.util.*;
import Donnees.Intersection;
import Donnees.DonneesCarte;
import Util.Coordonnees;

public class Carte extends Application {
    private Map<Intersection, Map<Intersection, Float>> graph;
    private String chemin;
    @Override
    public void start(Stage primaryStage) {
        // Create a sample graph
        float [] echelle= createSampleGraph();
        System.out.println(echelle[1]);

        // Create a container for the graph
        Pane graphContainer = new Pane();

        // Add nodes to the container
        for (Intersection inter : graph.keySet()) {
            //System.out.println(inter);
            graphContainer.getChildren().add(createNode(inter.getCoordonnees().getLatitude()*echelle[0],inter.getCoordonnees().getLongitude()*echelle[1]));
        }

        // Add edges to the container
        for (Map.Entry<Intersection, Map<Intersection, Float>> entry : graph.entrySet()) {
            Intersection sourceNode = entry.getKey();
            Map<Intersection, Float> edges = entry.getValue();

            for (Map.Entry<Intersection, Float> edge : edges.entrySet()) {
                Intersection targetNode = edge.getKey();
                float length = edge.getValue();

                graphContainer.getChildren().add(createEdge(createNode(sourceNode.getCoordonnees().getLatitude()*echelle[0],sourceNode.getCoordonnees().getLongitude()*echelle[1]), (createNode(targetNode.getCoordonnees().getLatitude()*echelle[0],targetNode.getCoordonnees().getLongitude()*echelle[1])), length));
            }
        }

        // Create a scene and set it in the stage
        Scene scene = new Scene(graphContainer, 1920, 1080);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Map Graph Visualization");
        primaryStage.show();
    }

    private float[] createSampleGraph() {
        Service s = new Service();
        DonneesCarte dc = s.creerDonneesCarte("smallMap.xml");
        graph = dc.getCarte();
        Intersection entrepot = dc.getEntrepot();
        Coordonnees c = (entrepot.getCoordonnees());
        Circle depart = createNode(entrepot.getCoordonnees().getLatitude() *dc.getEchelleX() ,entrepot.getCoordonnees().getLongitude()* dc.getEchelleY());

        for (int i = 0; i<graph.size();i++){


        }

        Circle node1 = createNode(100, 600);
        Circle node2 = createNode(200, 200);
        Circle node3 = createNode(300, 100);

       /* HashMap h1 = new HashMap<>();
        h1.put(node2, 10);
        h1.put(node3, 20);
        HashMap h3 = new HashMap<>();
        h3.put(node1, 10);
        h3.put(node3, 5);
        HashMap h2 = new HashMap<>();
        h2.put(node1, 20);
        h2.put(node2, 5);
        graph.put(node1, h1);
        graph.put(node2, h2);
        graph.put(node3, h3);*/
        return new float[]{dc.getEchelleX(), dc.getEchelleY()};
    }

    private Circle createNode(double x, double y) {
        Circle node = new Circle(3); // Circle with radius 10
        node.setCenterX(x);
        node.setCenterY(y);
        node.setStyle("-fx-fill: blue;");

        return node;
    }

    private Line createEdge(Circle source, Circle target, float length) {
        Line edge = new Line();

        edge.startXProperty().bind(source.centerXProperty());
        edge.startYProperty().bind(source.centerYProperty());

        edge.endXProperty().bind(target.centerXProperty());
        edge.endYProperty().bind(target.centerYProperty());

        edge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        edge.setAccessibleText(String.valueOf(length));
        return edge;
    }
    public void creerGraphe(String nomGraphe) {
        this.chemin = nomGraphe;
        launch();
    }
}
