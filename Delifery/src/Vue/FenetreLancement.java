package Vue;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

public class FenetreLancement extends Stage {

    private String cheminFichier;
    private Label monLabel = new Label();

    public FenetreLancement() {
        this.setTitle("Mon écran d'accueil");

        // Créez le contenu de votre fenêtre
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10); // Définir un espacement vertical entre les éléments


        // Ajoutez des éléments à root
        Button boutonLancement = new Button("Lancer l'application");
        boutonLancement.setOnAction(this::lancementButtonAction);
        root.getChildren().add(boutonLancement);

        // Définir le style du bouton pour que les contours soient noirs
        boutonLancement.setStyle("-fx-background-color: rgba(157, 157, 157, 0.5); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        boutonLancement.setOnMouseEntered(event -> {
            boutonLancement.setStyle("-fx-background-color: rgba(157, 157, 157, 0.7); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });
        boutonLancement.setOnMouseExited(event -> {
            boutonLancement.setStyle("-fx-background-color: rgba(157, 157, 157, 0.5); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });
        boutonLancement.setOnMousePressed(event -> {
            boutonLancement.setStyle("-fx-background-color: black; -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });


        // Ajoutez des éléments à root
        Button boutonImportation = new Button("Importer la carte");
        boutonImportation.setOnAction(this::importButtonAction);
        root.getChildren().add(boutonImportation);

        // Définir le style du bouton pour que les contours soient noirs
        boutonImportation.setStyle("-fx-background-color: rgba(125, 157, 165, 0.5); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        boutonImportation.setOnMouseEntered(event -> {
            boutonImportation.setStyle("-fx-background-color: rgba(125, 157, 165, 0.7); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });
        boutonImportation.setOnMouseExited(event -> {
            boutonImportation.setStyle("-fx-background-color: rgba(125, 157, 165, 0.5); -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });
        boutonImportation.setOnMousePressed(event -> {
            boutonImportation.setStyle("-fx-background-color: black; -fx-background-radius: 15px; -fx-border-width: 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });


        monLabel.setText("");
        monLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: rgba(255, 0, 0, 0.5);");
        root.getChildren().add(monLabel);


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
        File file = new File("Delifery/image/ImageAccueil.png");
        String imageUrl = file.toURI().toString();
        Image image = new Image(imageUrl);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                "-fx-background-position: top left; " +
                "-fx-background-repeat: no-repeat;");


        // Créer une ImageView pour l'image
        Image logoImage = new Image("file:Delifery/image/LogoDelifery.png");
        ImageView logoImageView = new ImageView(logoImage);

        // Réduire la taille de l'image
        double newWidth = this.getWidth() * 0.25; // Définissez la nouvelle largeur souhaitée
        double newHeight = (logoImage.getHeight() / logoImage.getWidth()) * newWidth; // Calculer la nouvelle hauteur pour maintenir le ratio
        logoImageView.setFitWidth(newWidth);
        logoImageView.setFitHeight(newHeight);

        root.getChildren().add(logoImageView);

        this.setScene(scene);
        this.show();

        // Définir la largeur préférée du bouton après l'affichage de la fenêtre
        boutonImportation.setPrefWidth(this.getWidth() * 0.18);
        boutonImportation.setPrefHeight(this.getHeight() * 0.07);

        // Définir la largeur préférée du bouton après l'affichage de la fenêtre
        boutonLancement.setPrefWidth(this.getWidth() * 0.2);
        boutonLancement.setPrefHeight(this.getHeight() * 0.08);


    }

    private void importButtonAction(javafx.event.ActionEvent actionEvent) {
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
            cheminFichier=filePath;
            // Affichage du chemin du fichier dans la console
            System.out.println(filePath);
            monLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: rgba(0, 255, 0, 0.5);");
            monLabel.setText("Fichier importé !");

        }else{
            monLabel.setText("Coucou");
        }
    }

    private void lancementButtonAction(javafx.event.ActionEvent actionEvent) {

        if (cheminFichier!=null){
            this.close();
            // fenetreMap=new FenetreMap();
            //fenetreMap.show();
            VueApplication vueApp = new VueApplication(cheminFichier);

            // Appelez la méthode start pour ouvrir la vue
            vueApp.start(new Stage());
            System.out.println("coucou");
        }else{
            monLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.5);");
            monLabel.setText("Aucun fichier importé...");

        }

    }


}
