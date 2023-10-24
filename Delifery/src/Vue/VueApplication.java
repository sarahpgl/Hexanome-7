package Vue;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class VueApplication extends Application {
    public String chemin;

    public VueApplication(String cheminFichier) {
        super();
        this.chemin = cheminFichier;
    }
    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();

        // Row 1: Header (takes full width)
        HBox header = new HBox();
        header.setStyle("-fx-background-color: lightblue;");
        gridPane.add(header, 0, 0, 2, 1); // Span 2 columns

        // Row 2: Section 1 (left side)
        HBox section1 = new HBox();
        section1.setStyle("-fx-background-color: lightgreen;");
        gridPane.add(section1, 0, 1);

        // Row 3: Section 2 (right side)
        Carte c = new Carte(chemin, 300, 400);
        gridPane.add(c, 1, 1);

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
        HBox.setHgrow(section1, Priority.ALWAYS);
        HBox.setHgrow(c, Priority.ALWAYS);

        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setTitle("Three Row GridPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetre() {
        launch();
    }
}
