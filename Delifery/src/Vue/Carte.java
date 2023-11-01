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
        float [] util = {originX,originY,echelleX,echelleY};


        // Création du point entrepôt
        Intersection entrepot = dc.getEntrepot();
        Circle entrepotLocation = createNode(entrepot, util,1);
        this.getChildren().add(entrepotLocation);

        //test de recupere un tour
        ArrayList<Intersection> sectionAColorier = testCalculChemin();
        Intersection destination3 = sectionAColorier.getFirst();
        sectionAColorier.remove(0);


        // Ajout des points de départ et  (en théorie c'est le meme : l'entrepot)
        for (Intersection inter : graph.keySet()) {
            if (inter != null){
                if(inter == entrepot || inter == destination3) {
                    // Ajout du point
                    Circle entrepot1 = createNode(inter,util, 2);
                    this.getChildren().add(entrepot1);
                }
             }
        }

        // Ajout des Livraisons entre les points arrivée et départ
        for (Map.Entry<Intersection, Map<Intersection, Float>> entry : graph.entrySet()) {
            Intersection sourceNode = entry.getKey();
            //test car sinon beug
            if (sourceNode!= null) {
                Map<Intersection, Float> edges = entry.getValue();
                for (Map.Entry<Intersection, Float> edge : edges.entrySet()) {
                    Intersection targetNode = edge.getKey();
                    float length = edge.getValue();
                    //si l'intersection fait partie du tour

                    if(sectionAColorier.contains(targetNode) && sectionAColorier.contains(sourceNode)) {

                        // Creation du point de départ
                        Circle depart = createNode(sourceNode,util, 2);

                        // Creation du point d'arrivée
                        Circle arrivee = createNode(targetNode,util, 2);

                        // Creation de l'arrête reliant les deux
                        Line edgeToAdd = createEdge(depart, arrivee, length, 2);
                        this.getChildren().add(edgeToAdd);

                    //si l'intersection ne fait pas partie du tour
                    }else{

                        // Creation du point de départ
                        Circle depart = createNode(sourceNode,util, null);

                        // Creation du point d'arrivée
                        Circle arrivee = createNode(targetNode,util, null);

                        // Creation de l'arrête reliant les deux
                        Line edgeToAdd = createEdge(depart, arrivee, length, 1);
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


        return dc;
    }

    private ArrayList<Intersection>  testCalculChemin(){
        Service service = new Service();
        Intersection Entrepot = new Intersection(new BigInteger("25303831"),new Coordonnees(45.74979,4.87572));
        Intersection destination1 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214,4.875591));
        Intersection destination2 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969,4.873468));
        Intersection destination3 = new Intersection(new BigInteger("27362284"), new Coordonnees(45.728672,4.876898));
        List<Intersection> chemin2 = service.dijkstra(Entrepot,destination3,graph);

        ArrayList<Intersection> monChemin = new ArrayList<Intersection>(chemin2);

        //recuperer les sections de mon chemin qui ne sont pas des livraisons
        ArrayList<Intersection> sectionAColorier = new ArrayList<Intersection>();
        sectionAColorier.add(destination3);
        for(Intersection i : monChemin){
            if(i != destination3 && i != Entrepot){
                sectionAColorier.add(i);
            }
        }
        return sectionAColorier;
    }

    private Circle createNode( Intersection inter, float [] util, Integer color) {
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
        if (color== null){
            node.setStyle("-fx-fill: black;");
            node.setRadius(1);
        }
        //pour colorer l'entrepôt en vert
        else if (color ==1){
            node.setStyle("-fx-fill: green;");
            node.setRadius(3);
        }

        else if(color == 2){
            node.setStyle("-fx-fill: red;");
            node.setRadius(3);
        }
        else if(color == 3){
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
        if(color == 2){
            edge.setStyle("-fx-stroke: red; -fx-stroke-width: 1;");
        }else{
            edge.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
        }
        return edge;
    }

}
