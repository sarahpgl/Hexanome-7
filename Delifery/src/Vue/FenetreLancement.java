package Vue;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
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

public class FenetreLancement extends Application {
    private String cheminFichier;
    private final Label monLabel = new Label();

    public void creerGraphe() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mon écran d'accueil");

        // Create the content of your window
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10); // Set vertical spacing between elements

        // Add elements to the root
        Button boutonLancement = new Button("Lancer l'application");
        boutonLancement.setOnAction(this::lancementButtonAction);

        // Set the button style with black borders
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

        // Add elements to root
        Button boutonImportation = new Button("Importer la carte");
        boutonImportation.setOnAction(this::importButtonAction);
        root.getChildren().add(boutonImportation);
        root.getChildren().add(boutonLancement);

        // Set the button style with black borders
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

        // Set the background image
        File file = new File("Delifery/image/ImageAccueil.png");
        String imageUrl = file.toURI().toString();
        Image image = new Image(imageUrl);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                "-fx-background-position: top left; " +
                "-fx-background-repeat: no-repeat;");

        // Create an ImageView for the image
        Image logoImage = new Image("file:Delifery/image/LogoDelifery.png");
        ImageView logoImageView = new ImageView(logoImage);

        // Resize the image
        double newWidth = primaryStage.getWidth() * 0.25;
        double newHeight = (logoImage.getHeight() / logoImage.getWidth()) * newWidth;
        logoImageView.setFitWidth(newWidth);
        logoImageView.setFitHeight(newHeight);

        root.getChildren().add(logoImageView);

        primaryStage.setScene(scene);
        primaryStage.show();

        // Set the preferred width of the "Import" button after the window is displayed
        boutonImportation.setPrefWidth(primaryStage.getWidth() * 0.18);
        boutonImportation.setPrefHeight(primaryStage.getHeight() * 0.07);

        // Set the preferred width of the "Launch" button after the window is displayed
        boutonLancement.setPrefWidth(primaryStage.getWidth() * 0.2);
        boutonLancement.setPrefHeight(primaryStage.getHeight() * 0.08);
    }

    private void importButtonAction(javafx.event.ActionEvent actionEvent) {

        // Create a FileChooser to open the file explorer
        FileChooser fileChooser = new FileChooser();
        // Set a filter to accept only XML files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml", "*.XML", "*.Xml"));
        fileChooser.setInitialDirectory(new File("Delifery"));

        File file = fileChooser.showOpenDialog(null);


        // Check if the file is not null
        if (null == null) {
            // Get the path of the selected file
            String filePath = file.getAbsolutePath();
            //String filePath="Delifery/fichiersXML2022/mediumMap.xml";
            cheminFichier = filePath;
            // Display the file path in the console
            System.out.println(filePath);
            monLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: rgba(0, 255, 0, 0.5);");
            monLabel.setText("Fichier importé !");
        } else {
            //monLabel.setText("");
        }
    }

    private void lancementButtonAction(javafx.event.ActionEvent actionEvent) {
        if (cheminFichier != null) {
            Node source = (Node)actionEvent.getSource();
            Stage stage= (Stage)source.getScene().getWindow();
            stage.close();
            Stage appStage = new Stage();
            VueApplication vueApplication = new VueApplication(cheminFichier, (int)(source.getScene().getWidth()), (int)(source.getScene().getHeight()));
            vueApplication.start(appStage);
        } else {
            monLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.5);");
            monLabel.setText("Aucun fichier importé...");
        }
    }
}
