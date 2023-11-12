package Vue;

import Donnees.*;
import Service.Service;
import Util.Coordonnees;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TableauTours extends StackPane {
    Service service = Service.getInstance();
    private CatalogueTours catalogueTours;
    private String cheminFchier;

    Carte carteTab=null;
    public TableauTours(Carte c,String cheminFichier) {
        this.carteTab=c;
        this.catalogueTours = service.getCatalogueTours();
        this.cheminFchier = cheminFichier;

        int nbColonnes = 3; // Largeur du tableau
        ArrayList<Tour> tours = catalogueTours.getCatalogue();


        if (tours.size() < 1) {
            //si la catalogue courat est vide
            Label titreVIDE = new Label("Vous n'avez actuellement pas de tour. Chargez un fichier de tour ou ajoutez une livraison.");
            titreVIDE.setAlignment(Pos.CENTER);
            titreVIDE.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
            // Crée un VBox et ajoute le titre et le GridPane

            Button boutonCharger = new Button("Charger un tour");
            Button boutonSauvegarder = new Button("Sauvegarder le tour");
            boutonCharger.setPrefWidth(230);
            boutonCharger.setPrefHeight(40);
            boutonSauvegarder.setPrefWidth(230);
            boutonSauvegarder.setPrefHeight(40);

            HBox hbox2boutons = new HBox(boutonCharger, boutonSauvegarder);
            hbox2boutons.setSpacing(10);
            hbox2boutons.setAlignment(Pos.BOTTOM_CENTER);
            Button boutonAjouter = new Button("Ajouter une livraison");
            boutonAjouter.setPrefWidth(230);
            boutonAjouter.setPrefHeight(40);
            boutonAjouter.setOnAction(event -> {
                VueLivraison vueLivraison = new VueLivraison();
                Stage stage = new Stage();
                vueLivraison.start(stage);
            });

            VBox vbox3boutons = new VBox(hbox2boutons, boutonAjouter);
            vbox3boutons.setSpacing(20);
            vbox3boutons.setAlignment(Pos.BOTTOM_CENTER);


            List<Button> boutons = Arrays.asList(boutonCharger, boutonSauvegarder, boutonAjouter);

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

            titreVIDE.setTranslateY(30);
            titreVIDE.setTranslateX(30);

            vbox3boutons.setTranslateY(-20);

            VBox.setVgrow(titreVIDE, Priority.ALWAYS);
            VBox.setVgrow(vbox3boutons, Priority.ALWAYS);

            titreVIDE.setAlignment(Pos.TOP_CENTER);

            VBox vboxTotal=new VBox(titreVIDE,vbox3boutons);
            getChildren().add(vboxTotal);


        } else { //catalagueTours non vide

            int nbLignes = tours.size() + 1;

            GridPane tableau = new GridPane();
            tableau.setAlignment(Pos.CENTER);
            tableau.setHgap(-1); // Supprime l'espace horizontal entre les cellules
            tableau.setVgap(-1); // Supprime l'espace vertical entre les cellules
            tableau.setTranslateY(0); // Définit une hauteur minimale pour le GridPane
            //System.out.println(getHeight());

            for (int i = 0; i < nbColonnes; i++) {
                for (int j = 0; j < nbLignes; j++) {
                    Rectangle rectangle = new Rectangle(140, 70);
                    rectangle.setStroke(Color.BLACK);
                    rectangle.setFill(Color.LIGHTGREY);

                    StackPane cellule = new StackPane();
                    cellule.getChildren().add(rectangle);

                    if (i == 0 && j > 0) {
                        rectangle.setFill(Color.rgb(new Random().nextInt(30, 255), new Random().nextInt(30, 256), new Random().nextInt(30, 256)));
                        Text text = new Text(String.valueOf(tours.get(j - 1).getId()));
                        tableau.add(cellule, i, j);
                        cellule.getChildren().add(text);

                        int finalJ = j;
                        rectangle.setOnMouseClicked(event -> {
                            //System.out.println(catalogueTours.toString());
                            //System.out.println(catalogueTours.getTourById(tours.get(finalJ-1).getId()));
                            //System.out.println("id tour"+tours.get(finalJ-1).getId());

                            Service.getInstance().ouvrirDetails(this.cheminFchier, tours.get(finalJ - 1).getId());
                            rectangle.setFill(Color.rgb(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)));
                        });

                    } else if (i == 2 && j > 0) {
                        CheckBox caseCocher = new CheckBox();
                        caseCocher.setText(String.valueOf(j));
                        caseCocher.setStyle("-fx-text-fill: transparent;");
                        caseCocher.setSelected(true);
                        tableau.add(cellule, i, j);
                        cellule.getChildren().add(caseCocher);

                        int finalJ = j;
                        caseCocher.setOnAction(event -> {
                            if (caseCocher.isSelected()) {
                                System.out.println(caseCocher.getText());
                                service.getCarte().remettreLigne(caseCocher.getText());
                            } else {
                                service.getCarte().enleverLigne(caseCocher.getText());
                            }
                        });

                    } else if (i == 1 && j > 0) {
                        //Text text = new Text("cc");
                        Text text = new Text(catalogueTours.getCatalogue().get(j - 1).getNomLivreur());
                        tableau.add(cellule, i, j);
                        cellule.getChildren().add(text);
                    } else if (i == 0 && j == 0) {
                        Text text = new Text("Tour N°");
                        rectangle.setFill(Color.WHITE);
                        tableau.add(cellule, i, j);
                        cellule.getChildren().add(text);
                    } else if (i == 1 && j == 0) {
                        Text text = new Text("Livreur");
                        rectangle.setFill(Color.WHITE);
                        tableau.add(cellule, i, j);
                        cellule.getChildren().add(text);
                    } else if (i == 2 && j == 0) {
                        Text text = new Text("Visible sur la map");
                        rectangle.setFill(Color.WHITE);
                        tableau.add(cellule, i, j);
                        cellule.getChildren().add(text);
                    } else {
                        tableau.add(cellule, i, j);

                    }

                }
            }
            Label titre = new Label("Cliquez sur un numéro pour obtenir les détails du tour");
            titre.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");

            TextArea textArea = new TextArea();
            textArea.setPrefSize(10, 10);
            textArea.setStyle("-fx-font-size:20px;-fx-text-alignment: center;-fx-alignment: center");

            Button boutonLivreur = new Button("Enregistrer");
            boutonLivreur.setPrefWidth(100);
            boutonLivreur.setOnAction(event -> {
                service.setNbLivreur(Integer.parseInt(textArea.getText()));
                service.updateCarte();
            });

            Label titreLivreur = new Label("Nombre de livreurs : ");
            titreLivreur.setStyle("-fx-font-style: italic; -fx-font-size: 18px; -fx-text-fill: black;");

            tableau.setAlignment(Pos.CENTER);
            VBox vboxTableau = new VBox(titre, tableau);
            vboxTableau.setAlignment(Pos.TOP_CENTER);
            vboxTableau.setTranslateY(20);


            HBox hboxlivreur = new HBox(textArea, boutonLivreur);
            hboxlivreur.setSpacing(14);
            hboxlivreur.setAlignment(Pos.BOTTOM_CENTER);


            HBox hboxlivreurwithtitle = new HBox(titreLivreur, hboxlivreur);
            hboxlivreurwithtitle.setSpacing(12);
            hboxlivreurwithtitle.setAlignment(Pos.BOTTOM_CENTER);
            hboxlivreurwithtitle.setTranslateX(-20);


            Button boutonCharger = new Button("Charger un tour");
            Button boutonSauvegarder = new Button("Sauvegarder le tour");
            boutonCharger.setPrefWidth(230);
            boutonCharger.setPrefHeight(40);
            boutonSauvegarder.setPrefWidth(230);
            boutonSauvegarder.setPrefHeight(40);

            HBox hbox2boutons = new HBox(boutonCharger, boutonSauvegarder);
            hbox2boutons.setSpacing(10);
            hbox2boutons.setAlignment(Pos.BOTTOM_CENTER);
            Button boutonAjouter = new Button("Ajouter une livraison");
            boutonAjouter.setPrefWidth(230);
            boutonAjouter.setPrefHeight(40);
            boutonAjouter.setOnAction(event -> {
                VueLivraison vueLivraison = new VueLivraison();
                Stage stage = new Stage();
                vueLivraison.start(stage);
            });

            VBox vbox3boutons = new VBox(hbox2boutons, boutonAjouter);
            vbox3boutons.setSpacing(20);
            vbox3boutons.setAlignment(Pos.BOTTOM_CENTER);


            List<Button> boutons = Arrays.asList(boutonCharger, boutonSauvegarder, boutonAjouter, boutonLivreur);

            for (Button bouton : boutons) {
                bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #7D9DA5; -fx-background-radius: 20;");
                bouton.setOnMouseEntered(event -> {
                    bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #5C7A8B; -fx-background-radius: 20;");
                });
                bouton.setOnMouseExited(event -> {
                    bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #7D9DA5; -fx-background-radius: 20;");
                });
                bouton.setOnMousePressed(event -> {
                    if (bouton == boutonLivreur) System.out.println(textArea.getText());
                    bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #86a6b8; -fx-background-radius: 20;");
                });
            }

            VBox vboxPartieBasse = new VBox(hboxlivreurwithtitle, vbox3boutons);
            vboxPartieBasse.setSpacing(20);
            vboxPartieBasse.setAlignment(Pos.BOTTOM_CENTER);
            vboxPartieBasse.setTranslateY(-5);

            VBox vboxtotal = new VBox(vboxTableau, vboxPartieBasse);
            VBox.setVgrow(vboxTableau, Priority.ALWAYS);
            VBox.setVgrow(vboxPartieBasse, Priority.ALWAYS);
            getChildren().add(vboxtotal);


        }
    }

    public CatalogueTours getCatalogueTours() {
        return catalogueTours;
    }

    public void setCatalogueTours(CatalogueTours catalogueTours) {
        this.catalogueTours = catalogueTours;
    }

    public String getCheminFchier() {
        return cheminFchier;
    }

    public void setCheminFchier(String cheminFchier) {
        this.cheminFchier = cheminFchier;
    }
}
