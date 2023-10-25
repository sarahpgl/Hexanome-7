package Vue;

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
        boutonAccueil.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
        boutonAccueil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Mettez ici le code à exécuter lorsque le bouton "Accueil" est cliqué
                System.out.println("Bouton Accueil cliqué !");
            }
        });

        // Charge l'image à partir d'un fichier
        File file = new File("Delifery/logo.png");
        String imageUrl = file.toURI().toString();
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80); // Ajustez la hauteur de l'image selon vos besoins
        imageView.setPreserveRatio(true); // Garde le ratio de l'image lors du redimensionnement

        boutonQuitter = new Button("Quitter l'application");
        boutonQuitter.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
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



