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
        FenetreLancement fenetreLancement=new FenetreLancement();
        fenetreLancement.show();
    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetreLancement() {
        launch();
    }

    public void ouvrirFenetreVueApp() {
        launch();
    }

}
