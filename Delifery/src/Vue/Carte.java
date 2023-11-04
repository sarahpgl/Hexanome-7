package Vue;

import Donnees.*;
import Service.Service;
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

        List<Line> redLines = new ArrayList<>();
        Circle redDot=new Circle();
        boolean redActivated=true;

        ElementListe elementListeRed=new ElementListe(redLines,redDot,redActivated);

        List<Line> blueLines = new ArrayList<>();
        Circle blueDot=new Circle();
        boolean blueActivated=true;

        ElementListe elementListeBlue=new ElementListe(blueLines,blueDot,blueActivated);

        listeCouleurs.add(elementListeRed);
        listeCouleurs.add(elementListeBlue);


        dessinerCarte();


        //test de recupere un tour
        Intersection destination3 = new Intersection(new BigInteger("27362284"), new Coordonnees(45.728672, 4.876898));
        Intersection destination4 = new Intersection(new BigInteger("1345284783"), new Coordonnees(45.752106, 4.8660784));
        Intersection destination5 = new Intersection(new BigInteger("459797866"), new Coordonnees(45.75379, 4.874625));
        Intersection destination6 = new Intersection(new BigInteger("9214919"), new Coordonnees(45.74021, 4.864795));

        this.getChildren().add(createNode(destination3,util,0,"3"));
        this.getChildren().add(createNode(destination4,util,0,"4"));
        this.getChildren().add(createNode(destination5,util,0,"5"));
        this.getChildren().add(createNode(destination6,util,0,"6"));

        ArrayList<Intersection> section1Tour1 = testCalculChemin(entrepot,destination5);
        ArrayList<Intersection> section2Tour1 = testCalculChemin(destination5,destination3);
        ArrayList<Intersection> section3Tour1 = testCalculChemin(destination3,entrepot);

        ArrayList<Intersection> section1Tour2 = testCalculChemin(entrepot,destination4);
        ArrayList<Intersection> section2Tour2 = testCalculChemin(destination4,destination6);

        Livraison livr1=new Livraison(1L ,destination3,Creneau.HUIT_NEUF,LocalTime.MIDNIGHT,LocalTime.MIDNIGHT);
        Livraison livr2=new Livraison(1L ,destination4,Creneau.HUIT_NEUF,LocalTime.MIDNIGHT,LocalTime.MIDNIGHT);

        Livraison livr3=new Livraison(1L ,destination5,Creneau.HUIT_NEUF,LocalTime.MIDNIGHT,LocalTime.MIDNIGHT);
        Livraison livr4=new Livraison(1L ,destination6,Creneau.HUIT_NEUF,LocalTime.MIDNIGHT,LocalTime.MIDNIGHT);

        ArrayList<Livraison> livraisons1 = new ArrayList<>();
        livraisons1.add(livr1);
        livraisons1.add(livr2);


        //dessinerTour(sectionAColorier2, 2, destination5);

        section2Tour1.addAll(section1Tour1);
        section3Tour1.addAll(section2Tour1);

        section2Tour2.addAll(section1Tour2);


        ArrayList<Intersection> cheminTotal = new ArrayList<>();
        Tour tour1=new Tour(1L,livraisons1 ,section3Tour1);
        dessinerTour(tour1.getTrajet(), 1, destination3);

        Tour tour2=new Tour(1L,livraisons1 ,section2Tour2);
        dessinerTour(tour2.getTrajet(), 2, destination3);

        section2Tour1.remove(0);
        section1Tour1.remove(0);

        Circle entrepotLocation = createNode(entrepot, util, 3,"Entrepot");
        this.getChildren().add(entrepotLocation);


        enleverLigne("1");
        enleverLigne("2");
        remettreLigne("1");
        remettreLigne("2");


    }

    private DonneesCarte createSampleGraph() {
        Service s = new Service();
        DonneesCarte dc = s.creerDonneesCarte(cheminFichier);
        graph = dc.getCarte();


        return dc;
    }

    private ArrayList<Intersection> testCalculChemin(Intersection dest1,Intersection dest2) {
        Service service = new Service();
        Intersection Entrepot = new Intersection(new BigInteger("25303831"), new Coordonnees(45.74979, 4.87572));

        List<Intersection> chemin = service.dijkstra(dest1, dest2, graph);

        ArrayList<Intersection> monChemin = new ArrayList<Intersection>(chemin);

        //recuperer les sections de mon chemin qui ne sont pas des livraisons
        ArrayList<Intersection> sectionAColorier = new ArrayList<Intersection>();

        sectionAColorier.add(dest1);
        sectionAColorier.add(dest2);

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
                        if (index >= 0 && index < listeCouleurs.size()) {
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

    void remettreLigne(String numTour) {
        int index = Integer.parseInt(numTour) - 1;
        if (index >= 0 && index < listeCouleurs.size()) {
            Color color = (index == 0) ? Color.RED : Color.BLUE;
            for (Line l : listeCouleurs.get(index).getLines()) {
                l.setStroke(color);
            }
            listeCouleurs.get(index).getDot().setFill(color);
            listeCouleurs.get(index).getDot().setStroke(color);
            listeCouleurs.get(index).getDot().setVisible(true);
            listeCouleurs.get(index).setEtat(true);
        }
    }


    void ouvrirDetails(int id){
        DetailsTour fenetreDetails=new DetailsTour(null);
        fenetreDetails.show();
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
