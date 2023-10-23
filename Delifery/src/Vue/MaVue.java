package Vue;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MaVue  extends Application{
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mon Application JavaFX");

        // Créez le contenu de votre fenêtre
        StackPane root = new StackPane();

        // Créez un rectangle central (remplacez cela par votre carte)
        Rectangle mapRectangle = new Rectangle(400, 300, Color.LIGHTGREY);

        // Ajoutez le rectangle à la racine (ou à tout autre conteneur)
        root.getChildren().add(mapRectangle);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetre() {
        launch();
    }

}
