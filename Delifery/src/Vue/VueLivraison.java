package Vue;

import Donnees.Creneau;
import Donnees.Intersection;
import Service.Service;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import Donnees.Livreur;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VueLivraison extends Application {
    public Integer windowWidth=800;
    public Integer windowHeight=600;

    public VueLivraison() {
    }
    @Override
    public void start(Stage primaryStage) {

        Service s = Service.getInstance();

        GridPane gridPane = new GridPane();

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

        // Row 1: Header (takes full width)
        Entete entete = new Entete(); // Créez une instance de votre entête personnalisée
        gridPane.add(entete, 0, 0, 2, 1); // Span 2 columns

        // Row 2 et 3: TextFields and Labels (below the header)
        Label label1 = new Label("Adresse :");
        label1.setFont(Font.font("Arial", 14));
        ArrayList<Intersection> listeIntersections = Service.getInstance().getAllIntersections();
        ComboBox<Intersection> intersectionComboBox = new ComboBox<>(FXCollections.observableArrayList(listeIntersections));
        intersectionComboBox.setPromptText("Sélectionnez une intersection");
        Label label2 = new Label("Livreur :");
        label2.setFont(Font.font("Arial", 14));
        ArrayList<String> listeLivreurs = Service.getInstance().getListLivreur();
        ComboBox<String> livreurComboBox = new ComboBox<>(FXCollections.observableArrayList(listeLivreurs));
        livreurComboBox.setPromptText("Sélectionnez un livreur");

        // Ajoutez les titres et les zones de texte à la grille (une en dessous de l'autre)
        gridPane.add(label1, 0, 1);
        gridPane.add(intersectionComboBox, 1, 1);
        gridPane.add(label2, 0, 2);
        gridPane.add(livreurComboBox, 1, 2);

        // Set column constraints for the labels and text fields
        ColumnConstraints labelColumnConstraints = new ColumnConstraints();
        labelColumnConstraints.setHalignment(HPos.RIGHT); // Alignez les labels à droite
        labelColumnConstraints.setHgrow(Priority.NEVER);
        labelColumnConstraints.setMinWidth(125); // Définissez une largeur minimale pour les titres
        ColumnConstraints textFieldColumnConstraints = new ColumnConstraints();
        textFieldColumnConstraints.setHalignment(HPos.LEFT); // Alignez les zones de texte à gauche
        textFieldColumnConstraints.setHgrow(Priority.ALWAYS);
        textFieldColumnConstraints.setMinWidth(50); // Définissez une largeur minimale pour les zones de texte
        gridPane.getColumnConstraints().addAll(labelColumnConstraints, textFieldColumnConstraints);

        // Row 3: Buttons with title (aligned in a row)
        Label label3 = new Label("Plage horaire :");
        label3.setFont(Font.font("Arial", 14));
        Button button1 = new Button("8h-9h");
        Button button2 = new Button("9h-10h");
        Button button3 = new Button("10h-11h");
        Button button4 = new Button("11h-12h");
        HBox buttonsRow3 = new HBox(10); // Espace de 10 pixels entre les boutons
        buttonsRow3.getChildren().addAll(label3, button1, button2, button3, button4);
        AtomicInteger horaire = new AtomicInteger(0);

        // Ajouter le style aux boutons de la ligne 3
        button1.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
        button2.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
        button3.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
        button4.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");

        button1.setOnAction(event -> {
            button1.setStyle("-fx-background-color: #5C7A8B; -fx-text-fill: black;");
            button2.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button3.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button4.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            horaire.set(8);
        });

        button2.setOnAction(event -> {
            button1.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button2.setStyle("-fx-background-color: #5C7A8B; -fx-text-fill: black;");
            button3.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button4.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            horaire.set(9);
        });

        button3.setOnAction(event -> {
            button1.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button2.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button3.setStyle("-fx-background-color: #5C7A8B; -fx-text-fill: black;");
            button4.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            horaire.set(10);
        });

        button4.setOnAction(event -> {
            button1.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button2.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button3.setStyle("-fx-background-color: #7d9da5; -fx-text-fill: black;");
            button4.setStyle("-fx-background-color: #5C7A8B; -fx-text-fill: black;");
            horaire.set(11);
        });

        // Ajoutez les boutons à la grille (alignés dans la même ligne)
        gridPane.add(buttonsRow3, 0, 3, 2, 1);
        gridPane.setVgap(20);

        // Créez un label pour afficher le message d'erreur
        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red; -fx-font-size: 9px;");
        gridPane.add(erreurLabel, 0, 5, 2, 1);

        // Row 4: Boutons Valider et Annuler
        Button boutonAnnuler = new Button("Annuler");
        boutonAnnuler.setOnAction(event -> {
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) boutonAnnuler.getScene().getWindow();
            stage.close();
        });
        Button boutonValider = new Button("Valider");

        boutonValider.setOnAction(event -> {
            try {
                Livreur livreur = new Livreur(livreurComboBox.getValue());
                Intersection intersection = intersectionComboBox.getValue();
                Creneau creneau = Creneau.HUIT_NEUF;
                switch (horaire.get()) {
                    case 8: creneau= Creneau.HUIT_NEUF; break;
                    case 9: creneau= Creneau.NEUF_DIX; break;
                    case 10: creneau= Creneau.DIX_ONZE; break;
                    case 11: creneau= Creneau.ONZE_DOUZE; break;
                    default: System.out.println("Pas de créneau sélectionné");
                }
                boolean reussi = Service.getInstance().essaieAjoutLivraisonAuTour(intersection, creneau, livreur);
                erreurLabel.setText(reussi ? "" : "Le livreur ne peut pas assurer cette livraison");
                erreurLabel.setText(!reussi ? "" : "Interstection : "+ intersection+", Livreur : " + livreur +", Creneau : "+ creneau);
            } catch (Exception e) {
                // En cas d'erreur, mettez à jour le texte de l'erreurLabel
                erreurLabel.setText("Une erreur est survenue : " + e.getMessage());
                // Vous pouvez également imprimer la trace de la pile pour plus d'informations sur l'erreur.
                e.printStackTrace();
            }
        });

        HBox buttonsRow4 = new HBox(10); // Espace de 10 pixels entre les boutons
        buttonsRow4.getChildren().addAll(boutonAnnuler, boutonValider);

        List<Button> boutons = Arrays.asList(boutonAnnuler, boutonValider);

        for (Button bouton : boutons) {
            bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #7D9DA5; -fx-background-radius: 20;");
            bouton.setOnMouseEntered(event -> {
                bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #5C7A8B; -fx-background-radius: 20;");
            });
            bouton.setOnMouseExited(event -> {
                bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #7D9DA5; -fx-background-radius: 20;");
            });
            bouton.setOnMousePressed(event -> {
                bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #86a6b8; -fx-background-radius: 20;");
            });
        }

        // Ajoutez les boutons à la grille (alignés dans la même ligne)
        gridPane.add(buttonsRow4, 0, 4, 2, 1);

        // Set row constraints to take the full page
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        RowConstraints row4 = new RowConstraints();
        RowConstraints row5 = new RowConstraints();
        row1.setPercentHeight(20);
        row2.setPercentHeight(20); // Ajustez la hauteur de la deuxième rangée selon vos besoins
        row3.setPercentHeight(20); // Ajustez la hauteur de la troisième rangée selon vos besoins
        row4.setPercentHeight(20); // Ajustez la hauteur de la troisième rangée selon vos besoins
        row5.setPercentHeight(20);
        gridPane.getRowConstraints().addAll(row1, row2, row3);

        Scene scene = new Scene(gridPane, windowWidth, windowHeight);
        primaryStage.setTitle("Fenetre Livraison");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour ouvrir la fenêtre
    public void ouvrirFenetre() {
        launch();
    }
}
