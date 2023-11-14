package Vue;

import Donnees.CatalogueTours;
import Donnees.Tour;
import Service.Service;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



public class TableauTours extends StackPane {
    Service service = Service.getInstance();
    Carte carteTab = null;
    private CatalogueTours catalogueTours;
    private String cheminFchier;


    /**
     * Le tableau affichant la liste des tours, il permet d'acceder au détail de tour par la suite
     * @param c Carte contenant les tours à afficher
     * @param cheminFichier Chemin vers le fichier de la carte
     */
    public TableauTours(Carte c, String cheminFichier) {
        this.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px;");
        this.carteTab = c;
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
            boutonCharger.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));
                File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());

                if (selectedFile != null) {
                    // Vous pouvez appeler la méthode lireXML(selectedFile.getAbsolutePath()) ici
                    String cheminFichierCatalogueXML = selectedFile.getAbsolutePath();
                    System.out.println("Chemin du fichier sélectionné (Print dans TableauTours) : " + cheminFichierCatalogueXML);
                    service.restituerTour(cheminFichierCatalogueXML);

                    System.out.println("Catalogue restauré (Print dans TableauTours) : " + service.getCatalogueTours().toString());
                }
            });

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

            titreVIDE.setTranslateY(10);
            titreVIDE.setTranslateX(30);

            vbox3boutons.setTranslateY(-20);

            VBox.setVgrow(titreVIDE, Priority.ALWAYS);
            VBox.setVgrow(vbox3boutons, Priority.ALWAYS);

            titreVIDE.setAlignment(Pos.TOP_CENTER);

            TextArea textArea = new TextArea();
            textArea.setPrefSize(10, 10);
            textArea.setStyle("-fx-font-size:20px;-fx-text-alignment: center;-fx-alignment: center");
            Label titreLivreur = new Label("Nombre de livreurs : ");
            titreLivreur.setStyle("-fx-font-style: italic; -fx-font-size: 18px; -fx-text-fill: black;");
            Button boutonLivreur = new Button("Enregistrer");
            boutonLivreur.setPrefWidth(100);
            boutonLivreur.setOnAction(event -> {
                service.setNbLivreur(Integer.parseInt(textArea.getText()));
                service.updateCarte();
                service.updatePanel();
            });

            HBox hboxlivreur = new HBox(textArea, boutonLivreur);
            hboxlivreur.setSpacing(14);
            hboxlivreur.setAlignment(Pos.BOTTOM_CENTER);
            HBox hboxlivreurwithtitle = new HBox(titreLivreur, hboxlivreur);
            hboxlivreurwithtitle.setSpacing(12);
            hboxlivreurwithtitle.setAlignment(Pos.BOTTOM_CENTER);
            hboxlivreurwithtitle.setTranslateX(-20);
            VBox vboxPartieBasse = new VBox(hboxlivreurwithtitle, vbox3boutons);
            vboxPartieBasse.setSpacing(40);
            vboxPartieBasse.setAlignment(Pos.BOTTOM_CENTER);
            vboxPartieBasse.setTranslateY(60);

            VBox vboxTotal = new VBox(titreVIDE, vboxPartieBasse);
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
                        int finalJ = j;
                        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE};

                        rectangle.setFill(colors[(finalJ-1) % 5]);

                        Text text = new Text(String.valueOf(tours.get(j - 1).getId()));
                        tableau.add(cellule, i, j);
                        cellule.getChildren().add(text);

                        rectangle.setOnMouseEntered(event -> {
                            Color color = (Color) rectangle.getFill();
                            rectangle.setFill(color.darker());
                            service.getCarte().surlignerLigne(finalJ-1);
                        });

                        rectangle.setOnMouseClicked(event -> {
                            Service.getInstance().ouvrirDetails(this.cheminFchier, tours.get(finalJ - 1).getId());
                        });

                        rectangle.setOnMouseExited(event -> {
                            Color color = (Color) rectangle.getFill();
                            rectangle.setFill(color.brighter());
                            service.getCarte().amincirLigne(finalJ-1);
                        });


                    } else if (i == 2 && j > 0) {
                        if (catalogueTours.getCatalogue().get(j-1).getLivraisons().size()>=1){
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
                        }else{
                            Text text = new Text("Aucun tour prévu");
                            tableau.add(cellule, i, j);
                            cellule.getChildren().add(text);                        }

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
                service.updatePanel();
            });

            Label titreLivreur = new Label("Nombre de livreurs : ");
            titreLivreur.setStyle("-fx-font-style: italic; -fx-font-size: 18px; -fx-text-fill: black;");

            tableau.setAlignment(Pos.CENTER);

            ScrollPane scroll=new ScrollPane(tableau);
            scroll.setFitToWidth(true);
            scroll.setPrefWidth(tableau.getWidth());
            scroll.setStyle("-fx-background-color:white;");
            VBox vboxTableau = new VBox(titre, scroll);
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
            boutonCharger.setPrefWidth(230);
            boutonCharger.setPrefHeight(40);
            boutonCharger.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));
                fileChooser.setInitialDirectory(new File("Delifery/catalogueSauvegardeXML"));
                File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
                if (selectedFile != null) {
                    String cheminFichierCatalogueXML = selectedFile.getAbsolutePath();
                    System.out.println("Chemin du fichier sélectionné (Print dans TableauTours) : " + cheminFichierCatalogueXML);
                    service.restituerTour(cheminFichierCatalogueXML);
                    System.out.println("Catalogue restauré (Print dans TableauTours) : " + service.getCatalogueTours().toString());
                }
            });

            Button boutonSauvegarder = new Button("Sauvegarder le tour");
            boutonSauvegarder.setPrefWidth(230);
            boutonSauvegarder.setPrefHeight(40);
            boutonSauvegarder.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));
                fileChooser.setTitle("Enregistrer le fichier XML");

                // Spécifier le répertoire initial par défaut
                fileChooser.setInitialDirectory(new File("Delifery/catalogueSauvegardeXML"));

                // Présaisir un nom de fichier par défaut
                LocalDateTime now = LocalDateTime.now();
                String dateHeure = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String nomFichierInitial = "SauvegardeCatalogueTour_" + dateHeure + ".xml";
                fileChooser.setInitialFileName(nomFichierInitial);

                // Afficher la boîte de dialogue de sélection de fichier
                File selectedFile = fileChooser.showSaveDialog(getScene().getWindow());

                if (selectedFile != null) {
                    // Récupérer le chemin du fichier et le nom du fichier choisi par l'utilisateur
                    String cheminFichierSauvegarde = selectedFile.getAbsolutePath();
                    System.out.println("Chemin du fichier sélectionné : " + cheminFichierSauvegarde);

                    // Vous pouvez également extraire le nom du fichier (y compris l'extension) si nécessaire
                    String nomFichier = selectedFile.getName();
                    System.out.println("Nom du fichier : " + nomFichier);

                    service.sauvegarderCatalogueTourXML(service.getCatalogueTours(), cheminFichierSauvegarde);
                }
            });

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
