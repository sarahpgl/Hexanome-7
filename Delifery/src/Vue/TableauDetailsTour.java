package Vue;

import Donnees.Intersection;
import Donnees.Livraison;
import Donnees.Tour;
import Util.Coordonnees;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class TableauDetailsTour extends StackPane {

    Tour tour;

    public TableauDetailsTour(Tour monTour) {
        this.tour = monTour;
        int nbColonnes = 4; // Taille du tableau

        ArrayList<Livraison> livraisonsDetails = tour.getLivraisons();
        System.out.println(livraisonsDetails.size());
        int nbLignes = livraisonsDetails.size() + 1;

        GridPane tableau = new GridPane();
        tableau.setAlignment(Pos.CENTER);
        tableau.setHgap(1); // Ajustez l'espacement horizontal entre les cellules
        tableau.setVgap(1); // Ajustez l'espacement vertical entre les cellules


        Livraison livCourante = null;
        for (int i = 1; i < nbLignes; i++) {
            livCourante = livraisonsDetails.get(i - 1);
            for (int j = 0; j < nbColonnes; j++) {

                Rectangle rectangle = new Rectangle(140, 70);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTGREY);

                StackPane cellule = new StackPane();
                cellule.getChildren().add(rectangle);

                if (j == 0 && i >= 1) {
                    // Affiche le numéro de colis dans la première ligne
                    Label colisNumeroLabel = new Label(String.valueOf(livCourante.getId()));
                    colisNumeroLabel.setAlignment(Pos.CENTER);
                    cellule.getChildren().add(colisNumeroLabel);
                } else if (j == 1 && i >= 1) {
                    // Affiche les coordonnees dans la deuxième ligne
                    Intersection inter = livCourante.getAdresse();
                    Coordonnees coordonnees = inter.getCoordonnees();
                    String coordonneesStr = "(" + coordonnees.getLatitude() + ", " + coordonnees.getLongitude() + ")";
                    Label coordonneesLabel = new Label(coordonneesStr);
                    coordonneesLabel.setAlignment(Pos.CENTER);
                    cellule.getChildren().add(coordonneesLabel);
                } else if (j == 2 && i >= 1) {
                    // Affiche l'heure d'arrivée dans la troisième ligne
                    Label heure1Label = new Label(livCourante.getHeureArrivee().toString());
                    heure1Label.setAlignment(Pos.CENTER);
                    cellule.getChildren().add(heure1Label);
                } else if (j == 3 && i >= 1) {
                    // Affiche l'heure de départ dans la quatrième ligne
                    Label heure2Label = new Label(livCourante.getHeureDepart().toString());
                    heure2Label.setAlignment(Pos.CENTER);
                    cellule.getChildren().add(heure2Label);
                } else if (i == 0 && j == 0) {
                    Text text = new Text("Colis N°");
                    rectangle.setFill(Color.WHITE);
                    cellule.getChildren().add(text);
                } else if (i == 0 && j == 1) {
                    Text text = new Text("Coordonnées");
                    rectangle.setFill(Color.WHITE);
                    cellule.getChildren().add(text);
                } else if (i == 0 && j == 2) {
                    Text text = new Text("Heure arrivée");
                    rectangle.setFill(Color.WHITE);
                    cellule.getChildren().add(text);
                } else if (i == 0 && j == 3) {
                    Text text = new Text("Heure départ");
                    rectangle.setFill(Color.WHITE);
                    cellule.getChildren().add(text);
                }
                tableau.add(cellule, j, i); // Ajoutez la cellule au tableau

            }
        }

        String[] texts = {"Colis N°", "Coordonnées", "Heure arrivée", "Heure départ"};
        for (int i = 0; i < 4; i++) {
            Rectangle rectangle = new Rectangle(140, 70);
            rectangle.setStroke(Color.BLACK);
            rectangle.setFill(Color.LIGHTGREY);

            StackPane cellule = new StackPane();
            cellule.getChildren().add(rectangle);

            Text text = new Text(texts[i]);
            rectangle.setFill(Color.WHITE);
            cellule.getChildren().add(text);

            tableau.add(cellule, i, 0);
        }


        Label livreurLabel = new Label("Livreur :" + tour.getLivreur().getNom());
        Label identifiantTourLabel = new Label("Identifiant du tour :" + tour.getId());
        livreurLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
        identifiantTourLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
        VBox labelsVBox = new VBox(livreurLabel, identifiantTourLabel);
        labelsVBox.setTranslateX(20);

        //livreurLabel.setTranslateY(-10); // Ajustez la position verticale
        //identifiantTourLabel.setTranslateY(-10); // Ajustez la position verticale

        // Créez un VBox et ajoutez les labels, le titre et le GridPane
        VBox vbox = new VBox(labelsVBox, tableau);
        vbox.setAlignment(Pos.CENTER);

        getChildren().add(vbox);
        setAlignment(Pos.CENTER); // Centre le VBox dans le StackPane
    }
}

