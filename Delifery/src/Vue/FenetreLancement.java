package Vue;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.event.ActionEvent;
import java.io.File;

import static javafx.application.Application.launch;

public class FenetreLancement extends Stage {
    public FenetreLancement() {
        this.setTitle("Mon écran d'accueil");

        // Créez le contenu de votre fenêtre
        VBox root = new VBox();
        root.setAlignment(javafx.geometry.Pos.CENTER); // Centrer le contenu

        // Ajoutez des éléments à root
        Button button = new Button("Lancer l'application");
        button.setOnAction(this::handleButtonAction);
        root.getChildren().add(button);

        // Définir le style du bouton pour que les contours soient noirs
        button.setStyle("-fx-background-color: rgba(125, 157, 165, 0.5); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: rgba(125, 157, 165, 0.7); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });

        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: rgba(125, 157, 165, 0.5); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });

        button.setOnMousePressed(event -> {
            button.setStyle("-fx-background-color: black; -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });

        Scene scene = new Scene(root);

        // Obtenez les dimensions de l'écran
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Définissez la taille de la fenêtre en conséquence
        double width = screenBounds.getWidth() * 0.8;
        double height = 1020.0 / 1080.0 * screenBounds.getHeight();
        this.setWidth(width);
        this.setHeight(height);

        // Placez la fenêtre en haut à gauche de l'écran
        this.setX(0);
        this.setY(0);

        // Définissez l'image de fond
        File file = new File("Delifery/ImageAccueil.png");
        String imageUrl = file.toURI().toString();
        Image image = new Image(imageUrl);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                "-fx-background-position: top left; " +
                "-fx-background-repeat: no-repeat;");

        this.setScene(scene);
        this.show();

        // Définir la largeur préférée du bouton après l'affichage de la fenêtre
        button.setPrefWidth(this.getWidth() * 0.2);
        button.setPrefHeight(this.getHeight() * 0.08);

    }

    private void handleButtonAction(javafx.event.ActionEvent actionEvent) {
        // Création d'un objet FileChooser pour ouvrir l'explorateur de fichiers
        FileChooser fileChooser = new FileChooser();
        // Définition du filtre pour n'accepter que les fichiers XML

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml", "*.XML", "*.Xml"));
        // Affichage de la boîte de dialogue pour choisir un fichier
        File file = fileChooser.showOpenDialog(this);
        // Vérification que le fichier n'est pas null
        if (file != null) {
            // Récupération du chemin du fichier choisi
            String filePath = file.getAbsolutePath();
            // Affichage du chemin du fichier dans la console
            System.out.println(filePath);
            // Ici, vous pouvez faire ce que vous voulez avec le chemin du fichier, par exemple le passer à une autre méthode ou le stocker dans une variable
        }
    }


}
