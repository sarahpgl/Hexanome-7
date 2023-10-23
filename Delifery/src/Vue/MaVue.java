package Vue;
import Donnees.DonneesCarte;
import Donnees.Intersection;
import Service.Service;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import Service.Service;
import javafx.scene.layout.HBox;

public class MaVue  extends Application{
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mon Application JavaFX");
        Service s = new Service();
               
        // Créez le contenu de votre fenêtre



        // Créez un rectangle central (remplacez cela par votre carte)
        Rectangle mapRectangle = new Rectangle(400, 300, Color.LIGHTGREY);

        Circle point = new Circle(150.0f,150.0f,10, Color.BLUE);

        // Ajoutez le rectangle à la racine (ou à tout autre conteneur)



        //HBox hbox = new HBox(); // spacing = 8
        //hbox.getChildren().addAll(point);

        Group root = new Group(point);

        // Ajoutez des éléments à root

        Scene scene = new Scene(root, 1920, 1080);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetre() {
        launch();
    }

}
