
package Vue;
import Donnees.Tour;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DetailsTour extends Application {
    private Tour tour;
    private String cheminFichier;
    public Integer windowWidth;
    public Integer windowHeight;

    /**
     * Constructeur de DetailsTour, la fenêtre affiche un zoom de la carte, et un tableau de détail du tour
     * @param cheminFichier Chemin vers le fichier de la carte
     * @param tour Le tour a afficher en détail
     * @param width Largeur de la fenêtre
     * @param height Hauteur de la fenêtre
     */
    public DetailsTour(String cheminFichier, Tour tour, Integer width, Integer height) {
        super();
        this.windowWidth = width;
        this.windowHeight = height;
        this.tour = tour;
        this.cheminFichier = cheminFichier;
    }
    @Override
    public void start(Stage primaryStage) {

        GridPane gridPane = new GridPane();

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

        // Row 3: Section 2 (right side)
        Carte c = new Carte(cheminFichier, windowWidth/2, windowHeight*8/10,tour);
        gridPane.add(c, 1, 1);
        // Row 1: Header (takes full width)
        Entete entete=new Entete();
        gridPane.add(entete, 0, 0, 2, 1); // Span 2 columns



        // Row 2: Section 1 (left side)
        if(tour!=null){
            System.out.println("c laaa"+tour.toString());
            TableauDetailsTour tabDetailTour = new TableauDetailsTour(tour);
            gridPane.add(tabDetailTour, 0, 1);
            gridPane.requestLayout();
            // Make Section 1 and Section 2 equally growable
            HBox.setHgrow(tabDetailTour, Priority.ALWAYS);
            HBox.setHgrow(c, Priority.ALWAYS);
        }


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



        Scene scene = new Scene(gridPane, windowWidth, windowHeight);
        primaryStage.setTitle("Details tour");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetre() {
        launch();
    }
}
