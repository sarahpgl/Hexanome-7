
package Vue;
import Donnees.Tour;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class DetailsTour extends Stage {

    private Tour tour;

    public DetailsTour(Tour tour) {
        this.tour = tour;

        VBox vbox = new VBox();
        // Ajoutez ici les éléments que vous voulez afficher dans la fenêtre.
        // Par exemple, vous pouvez créer des labels pour afficher les détails du tour.

        Scene scene = new Scene(vbox, 300, 200);
        this.setScene(scene);
    }
}
