package Vue;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MaVue  extends Application{
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ma Application JavaFX");

        // Créez le contenu de votre fenêtre
        VBox root = new VBox();
        // Ajoutez des éléments à root

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetre() {
        launch();
    }

}
