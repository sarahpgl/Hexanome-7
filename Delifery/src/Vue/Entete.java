package Vue;

import Service.Service;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;

public class Entete extends HBox {

    Button boutonAccueil;
    Button boutonQuitter;

    public Entete() {
        this.setStyle("-fx-background-color: #ced4d3; -fx-padding: 10px;");

        boutonAccueil = new Button("Accueil");
        // On ajoute un effet de survol sur le bouton
        boutonAccueil.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
        boutonAccueil.setOnMouseEntered(e -> boutonAccueil.setStyle("-fx-background-color: #ced4d3; -fx-text-fill: black; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);"));
        boutonAccueil.setOnMouseExited(e -> boutonAccueil.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);"));
        boutonAccueil.setOnAction(e -> {
            new FenetreLancement().start(new Stage());
            ((Stage) boutonAccueil.getScene().getWindow()).close();
            Service.getInstance().setNbLivreur(0);
        });


        // Charge l'image à partir d'un fichier
        File file = new File("Delifery/logo.png");
        String imageUrl = file.toURI().toString();
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80); // Ajustez la hauteur de l'image selon vos besoins
        imageView.setPreserveRatio(true); // Garde le ratio de l'image lors du redimensionnement

        boutonQuitter = new Button("Quitter l'application");
        // On ajoute un effet de survol sur le bouton
        boutonQuitter.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
        boutonQuitter.setOnMouseEntered(e -> boutonQuitter.setStyle("-fx-background-color: #ced4d3; -fx-text-fill: black; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);"));
        boutonQuitter.setOnMouseExited(e -> boutonQuitter.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);"));
        boutonQuitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Mettez ici le code à exécuter lorsque le bouton "Quitter" est cliqué
                Stage stage = (Stage) getScene().getWindow();
                stage.close(); // Ferme la fenêtre
            }
        });

        this.getChildren().addAll(boutonAccueil, imageView, boutonQuitter);
        this.setAlignment(Pos.CENTER); // Centre les éléments horizontalement dans l'HBox
        this.setSpacing(200); // Ajoute un espacement entre les éléments
    }
}



