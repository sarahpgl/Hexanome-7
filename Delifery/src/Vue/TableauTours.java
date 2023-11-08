package Vue;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TableauTours extends StackPane  {

    Carte carteTab;

    public TableauTours(Carte carte) {
        carteTab=carte;
        int nbColonnes = 3; // Taille du tableau
        int nbLignes= carteTab.getNbTours()+1;

        GridPane tableau = new GridPane();
        tableau.setHgap(-1); // Supprime l'espace horizontal entre les cellules
        tableau.setVgap(-1); // Supprime l'espace vertical entre les cellules
        tableau.setTranslateY(0); // Définit une hauteur minimale pour le GridPane
        //System.out.println(getHeight());

        for (int i = 0; i < nbColonnes; i++) {
            for (int j = 0; j < nbLignes; j++) {
                Rectangle rectangle = new Rectangle(140,70);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTGREY);

                StackPane cellule = new StackPane();
                cellule.getChildren().add(rectangle);

                if(i==0 && j>0){
                    rectangle.setFill(Color.rgb(new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256)));
                    Text text = new Text(String.valueOf(j));
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);


                    Long finalJ = (long) j;
                    rectangle.setOnMouseClicked(event ->{
                        carteTab.ouvrirDetails(finalJ);

                        rectangle.setFill(Color.rgb(new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256)));
                    });


                }
                else if(i==2 && j>0){
                    CheckBox caseCocher=new CheckBox();
                    caseCocher.setText(String.valueOf(j));
                    caseCocher.setSelected(true);
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(caseCocher);

                    int finalJ = j;
                    caseCocher.setOnAction(event -> {
                        if (caseCocher.isSelected()) {
                            System.out.println(caseCocher.getText());
                            carteTab.remettreLigne(caseCocher.getText());
                        } else {
                            carteTab.enleverLigne(caseCocher.getText());
                        }
                    });

                }
                else if(i==1 && j==1){
                    Text text = new Text("Tour Rouge");
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);

                }
                else if(i==1 && j==2){
                    Text text = new Text("Tour Bleu");
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);

                }else if(i==0 && j==0){
                    Text text = new Text("Tour N°");
                    rectangle.setFill(Color.WHITE);
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);
                }
                else if(i==1 && j==0){
                    Text text = new Text("Livreur");
                    rectangle.setFill(Color.WHITE);
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);
                }
                else if(i==2 && j==0){
                    Text text = new Text("Visible sur la map");
                    rectangle.setFill(Color.WHITE);
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);
                }
                else{
                    tableau.add(cellule, i, j);

                }

            }
        }
        Label titre = new Label("Cliquez sur un numéro pour obtenir les détails du tour");
        titre.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");

        TextArea textArea = new TextArea();
        textArea.setPrefSize(10,10);
        textArea.setStyle("-fx-font-size:20px;-fx-text-alignment: center;-fx-alignment: center");

        Button boutonLivreur = new Button("Enregistrer");
        boutonLivreur.setPrefWidth(100);

        Label titreLivreur=new Label("Nombre de livreurs : ");
        titreLivreur.setStyle("-fx-font-style: italic; -fx-font-size: 18px; -fx-text-fill: black;");

        tableau.setAlignment(Pos.CENTER);
        VBox vboxTableau = new VBox(titre, tableau);
        vboxTableau.setAlignment(Pos.TOP_CENTER);
        vboxTableau.setTranslateY(20);


        HBox hboxlivreur = new HBox(textArea, boutonLivreur);
        hboxlivreur.setSpacing(14);
        hboxlivreur.setAlignment(Pos.BOTTOM_CENTER);


        HBox hboxlivreurwithtitle = new HBox(titreLivreur,hboxlivreur);
        hboxlivreurwithtitle.setSpacing(12);
        hboxlivreurwithtitle.setAlignment(Pos.BOTTOM_CENTER);
        hboxlivreurwithtitle.setTranslateX(-20);


        Button boutonCharger=new Button("Charger un tour");
        Button boutonSauvegarder=new Button("Sauvegarder le tour");
        boutonCharger.setPrefWidth(230);
        boutonCharger.setPrefHeight(40);
        boutonSauvegarder.setPrefWidth(230);
        boutonSauvegarder.setPrefHeight(40);

        HBox hbox2boutons=new HBox(boutonCharger,boutonSauvegarder);
        hbox2boutons.setSpacing(10);
        hbox2boutons.setAlignment(Pos.BOTTOM_CENTER);
        Button boutonAjouter=new Button("Ajouter une livraison");
        boutonAjouter.setPrefWidth(230);
        boutonAjouter.setPrefHeight(40);
        boutonAjouter.setOnAction(event -> {
            VueLivraison vueLivraison = new VueLivraison();
            Stage stage = new Stage();
            vueLivraison.start(stage);
        });

        VBox vbox3boutons=new VBox(hbox2boutons,boutonAjouter);
        vbox3boutons.setSpacing(20);
        vbox3boutons.setAlignment(Pos.BOTTOM_CENTER);


        List<Button> boutons = Arrays.asList(boutonCharger, boutonSauvegarder, boutonAjouter,boutonLivreur);

        for (Button bouton : boutons) {
            bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #7D9DA5; -fx-background-radius: 20;");
            bouton.setOnMouseEntered(event -> {
                bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #5C7A8B; -fx-background-radius: 20;");
            });
            bouton.setOnMouseExited(event -> {
                bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #7D9DA5; -fx-background-radius: 20;");
            });
            bouton.setOnMousePressed(event -> {
                if (bouton==boutonLivreur)System.out.println(textArea.getText());
                bouton.setStyle("-fx-font-size: 14px; -fx-text-fill: black;-fx-background-color: #86a6b8; -fx-background-radius: 20;");
            });
        }

        VBox vboxPartieBasse=new VBox(hboxlivreurwithtitle,vbox3boutons);
        vboxPartieBasse.setSpacing(20);
        vboxPartieBasse.setAlignment(Pos.BOTTOM_CENTER);
        vboxPartieBasse.setTranslateY(-5);

        VBox vboxtotal= new VBox(vboxTableau,vboxPartieBasse);
        VBox.setVgrow(vboxTableau, Priority.ALWAYS);
        VBox.setVgrow(vboxPartieBasse, Priority.ALWAYS);
        getChildren().add(vboxtotal);


    }


}
