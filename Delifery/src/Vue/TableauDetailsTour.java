package Vue;

import Donnees.Livraison;
import Util.Coordonnees;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;
import Donnees.Tour;
import Donnees.Intersection;
import Donnees.Livraison;
public class TableauDetailsTour extends StackPane {

    Tour tour;

    public TableauDetailsTour(Tour tour) {
        this.tour = tour;
        int nbColonnes = 4; // Taille du tableau

        ArrayList<Livraison> livraisonsDetails = tour.getLivraisons();
        int nbLignes = livraisonsDetails.size() + 1;

        GridPane tableau = new GridPane();
        tableau.setAlignment(Pos.CENTER);
        tableau.setHgap(1); // Ajustez l'espacement horizontal entre les cellules
        tableau.setVgap(1); // Ajustez l'espacement vertical entre les cellules
        tableau.setTranslateY(-50); // Définit une hauteur minimale pour le GridPane

        // Créez des labels pour les noms de colonnes
        Label colisLabel = new Label("Colis");
        Label adresseLabel = new Label("Adresse");
        Label heureArriveeLabel = new Label("Heure d'Arrivée");
        Label heureDepartLabel = new Label("Heure de Départ");

        // Ajoutez les labels à la première ligne du tableau
        tableau.add(colisLabel, 0, 0);
        tableau.add(adresseLabel, 1, 0);
        tableau.add(heureArriveeLabel, 2, 0);
        tableau.add(heureDepartLabel, 3, 0);

        Label livreurLabel = new Label("Livreur :");
        Label identifiantTourLabel = new Label("Identifiant du tour :");

        VBox labelsVBox = new VBox(livreurLabel, identifiantTourLabel);

        for (int i = 0; i < nbColonnes; i++) {
            Livraison livCourante = livraisonsDetails.get(i);

            for (int j = 0; j < nbLignes; j++) {
                Rectangle rectangle = new Rectangle(140, 70);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTGREY);

                StackPane cellule = new StackPane();
                cellule.getChildren().add(rectangle);

                if (j == 0) {
                    // Affiche le numéro de colis dans la première ligne
                    Label colisNumeroLabel = new Label(String.valueOf(livCourante.getId()));
                    colisNumeroLabel.setAlignment(Pos.CENTER);
                    cellule.getChildren().add(colisNumeroLabel);
                } else if (j == 1) {
                    // Affiche les coordonnees dans la deuxième ligne
                    Intersection inter = livCourante.getAdresse();
                    Coordonnees coordonnees = inter.getCoordonnees();
                    String coordonneesStr = "(" + coordonnees.getLatitude() + ", " + coordonnees.getLongitude() + ")";
                    Label coordonneesLabel = new Label(coordonneesStr);
                    coordonneesLabel.setAlignment(Pos.CENTER);
                    cellule.getChildren().add(coordonneesLabel);
                } else if (j == 2) {
                    // Affiche l'heure d'arrivée dans la troisième ligne
                    Label heure1Label = new Label(livCourante.getHeureArrivee().toString());
                    heure1Label.setAlignment(Pos.CENTER);
                    cellule.getChildren().add(heure1Label);
                } else if (j == 3) {
                    // Affiche l'heure de départ dans la quatrième ligne
                    Label heure2Label = new Label(livCourante.getHeureDepart().toString());
                    heure2Label.setAlignment(Pos.CENTER);
                    cellule.getChildren().add(heure2Label);
                }

                tableau.add(cellule, i, j); // Ajoutez la cellule au tableau
            }
        }

        livreurLabel.setAlignment(Pos.CENTER);
        identifiantTourLabel.setAlignment(Pos.CENTER);
        livreurLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
        identifiantTourLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
        livreurLabel.setTranslateY(-10); // Ajustez la position verticale
        identifiantTourLabel.setTranslateY(-10); // Ajustez la position verticale

        // Créez un VBox et ajoutez les labels, le titre et le GridPane
        VBox vbox = new VBox(labelsVBox, tableau);
        vbox.setAlignment(Pos.CENTER);

        getChildren().add(vbox);
        setAlignment(Pos.CENTER); // Centre le VBox dans le StackPane
    }
}

