package Vue;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.File;

public class FenetreAccueil extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mon écran d'accueil");

        // Créez le contenu de votre fenêtre
        VBox root = new VBox();
        root.setAlignment(javafx.geometry.Pos.CENTER); // Centrer le contenu

        // Ajoutez des éléments à root
        Button button = new Button("Lancer l'application");
        root.getChildren().add(button);

        // Définir le style du bouton pour que les contours soient noirs
        button.setStyle("-fx-background-color: rgba(125, 157, 165, 0.5); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");


        Scene scene = new Scene(root);

        // Obtenez les dimensions de l'écran
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Définissez la taille de la fenêtre en conséquence
        double width = screenBounds.getWidth() * 0.8;
        double height = 1020.0 / 1080.0 * screenBounds.getHeight();
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);

        // Placez la fenêtre en haut à gauche de l'écran
        primaryStage.setX(0);
        primaryStage.setY(0);

        // Définissez l'image de fond
        File file = new File("Delifery/ImageAccueil.png");
        String imageUrl = file.toURI().toString();
        Image image = new Image(imageUrl);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                "-fx-background-position: top left; " +
                "-fx-background-repeat: no-repeat;");

        primaryStage.setScene(scene);
        primaryStage.show();

        // Définir la largeur préférée du bouton après l'affichage de la fenêtre
        button.setPrefWidth(primaryStage.getWidth() * 0.2);
        button.setPrefHeight(primaryStage.getHeight() * 0.08);

    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetre() {
        launch();
    }
}
