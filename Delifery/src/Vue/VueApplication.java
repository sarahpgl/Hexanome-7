package Vue;
import Donnees.CatalogueTours;
import Donnees.Tour;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;
import Service.Service;

public class VueApplication extends Application {
    public String cheminFichier;
    public Integer windowWidth;
    public Integer windowHeight;
    GridPane gridPane = new GridPane();

    public Carte carte;
    public TableauTours tableauTours;

    Service service = Service.getInstance();

    /**
     * Fenêtre de base de l'application qui accueille les différents panneaux
     * @param cheminFichier chemin du fichier vers la carte
     * @param width largeur de la fenêtre
     * @param height hauteur de la carte
     */
    public VueApplication(String cheminFichier, Integer width, Integer height) {
        super();
        this.windowWidth = width;
        this.windowHeight = height;
        this.cheminFichier = cheminFichier;
    }

    @Override
    public void start(Stage primaryStage) {


        // Get screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        // Set window size accordingly
        double width = screenBounds.getWidth() * 0.8;
        double height = 1020.0 / 1080.0 * screenBounds.getHeight();
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        // Place the window at the top-left of the screen
        primaryStage.setX(0);
        primaryStage.setY(0);

        // Row 1: Header (takes full width)
        Entete entete = new Entete();
        gridPane.add(entete, 0, 0, 2, 1); // Span 2 columns


        // Row 3: Section 2 (right side)
        Carte c = new Carte(cheminFichier, windowWidth / 2, windowHeight * 8 / 10);
        this.carte = c;
        gridPane.add(c, 1, 1);

        CatalogueTours ct = Service.getInstance().getCatalogueTours();

        // Row 2: Section 1 (left side)

        TableauTours tabTours = new TableauTours(c, this.cheminFichier);
        this.tableauTours = tabTours;
        gridPane.add(tabTours, 0, 1);
        gridPane.requestLayout();
        HBox.setHgrow(tabTours, Priority.ALWAYS);


        // Set column constraints to divide the width equally
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        column1.setPercentWidth(50);
        column2.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(column1, column2);

        // Set row constraints to take the full page
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        row1.setPercentHeight(20);
        row2.setPercentHeight(80);
        gridPane.getRowConstraints().addAll(row1, row2);

        // Make Section 1 and Section 2 equally growable

        HBox.setHgrow(c, Priority.ALWAYS);

        service.setVueApplication(this);

        Scene scene = new Scene(gridPane, windowWidth, windowHeight);
        primaryStage.setTitle("Fenetre Application");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Recharge le fichier de la carte à partir de <b>CheminFichier</b>
     */
    public void updateCarte() {
        gridPane.getChildren().remove(this.carte);
        Carte c = new Carte(cheminFichier, windowWidth / 2, windowHeight * 8 / 10);
        this.carte = c;
        gridPane.add(c, 1, 1);
    }

    /**
     * Mets à jour le tableau tour en fonction de la <b>carte</b> et du <b>cheminFichier</b>
     */
    public void updatePanel() {
        gridPane.getChildren().remove(this.tableauTours);
        TableauTours tb = new TableauTours(this.carte,this.cheminFichier);

        this.tableauTours = tb;
        gridPane.add(tb, 0, 1);
        gridPane.requestLayout();
        HBox.setHgrow(tb, Priority.ALWAYS);

    }


    // Méthode pour ouvrir la fenêtre
    /*public void ouvrirFenetre() {
        launch();
    }*/
}
