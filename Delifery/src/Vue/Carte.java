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
    Map<Intersection, Map<Intersection, Float>> graph;
    String chemin;

    public Carte(String cheminFichier){
        chemin=cheminFichier;
    }
    @Override
    public void start(Stage primaryStage) {

        // Create a sample graph
        DonneesCarte dc = createSampleGraph();
        // Extraction de l'echelle pour plus de lisibilité
        float [] echelle= {dc.getEchelleX(), dc.getEchelleY(), dc.getOrigine()[0], dc.getOrigine()[1]};


        // Create a container for the graph
        Pane graphContainer = new Pane();


        // Création du point entrepôt
        Intersection entrepot = dc.getEntrepot();
        graphContainer.getChildren().add(createNode((entrepot.getCoordonnees().getLatitude()-echelle[2])*echelle[0],(entrepot.getCoordonnees().getLongitude()-echelle[3])*echelle[1],1));

        // Add nodes to the container
        for (Intersection inter : graph.keySet()) {
            if (inter != null){
                graphContainer.getChildren().add(createNode((inter.getCoordonnees().getLatitude()-echelle[2])*echelle[0],(inter.getCoordonnees().getLongitude()-echelle[3])*echelle[1],null));
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
                    graphContainer.getChildren().add(createEdge(createNode((sourceNode.getCoordonnees().getLatitude() - echelle[2]) * echelle[0], (sourceNode.getCoordonnees().getLongitude() - echelle[3]) * echelle[1], null), (createNode((targetNode.getCoordonnees().getLatitude() - echelle[2]) * echelle[0], (targetNode.getCoordonnees().getLongitude() - echelle[3]) * echelle[1],null)), length, 0));

                }
            }
        }

        // Create a scene and set it in the stage
        Scene scene = new Scene(graphContainer, 800, 800);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Map Graph Visualization");
        primaryStage.show();
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
        return node;
    }

    private Line createEdge(Circle source, Circle target, float length, int color) {
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
