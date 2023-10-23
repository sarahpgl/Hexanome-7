package Vue;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MaVue  extends Application{
    @Override
    public void start(Stage primaryStage) {
        FenetreLancement fenetreLancement=new FenetreLancement();
        fenetreLancement.show();
    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetre() {
        launch();
    }

}
