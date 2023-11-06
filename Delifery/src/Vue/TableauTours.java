package Vue;

import Donnees.CatalogueTours;
import Donnees.Livreur;
import Donnees.Tour;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;
import Service.Service;


public class TableauTours extends StackPane  {

    private CatalogueTours catalogueTour;
    private String chemin;

    public TableauTours(CatalogueTours catalogueTour, String chemin) {

        this.catalogueTour = catalogueTour;
        this.chemin = chemin;

        int nbColonnes = 3; // Taille du tableau

        ArrayList<Tour> tours = catalogueTour.getCatalogue();
        int nbLignes= tours.size()+1;

        GridPane tableau = new GridPane();
        tableau.setAlignment(Pos.CENTER);
        tableau.setHgap(-1); // Supprime l'espace horizontal entre les cellules
        tableau.setVgap(-1); // Supprime l'espace vertical entre les cellules
        tableau.setTranslateY(-50); // Définit une hauteur minimale pour le GridPane
        //System.out.println(getHeight());

        for (int i = 0; i < nbColonnes; i++) {
            for (int j = 0; j < nbLignes; j++) {
                Rectangle rectangle = new Rectangle(140,70);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTGREY);

                Tour tourCourant = tours.get(j);

                StackPane cellule = new StackPane();
                cellule.getChildren().add(rectangle);

                if(i==0 && j>0){
                    rectangle.setFill(Color.rgb(new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256)));
                    Text text = new Text(String.valueOf(j));
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);

                    Long finalJ = (long) j;
                    /*rectangle.setOnMouseClicked(event ->{
                        Service.getInstance().ouvrirDetails(chemin,finalJ);
                        rectangle.setFill(Color.rgb(new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256)));
                    });*/

                }else if(i==2 && j>0){
                    CheckBox caseCocher=new CheckBox();
                    caseCocher.setText(String.valueOf(j));
                    caseCocher.setSelected(true);
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(caseCocher);

                    int finalJ = j;
                    caseCocher.setOnAction(event -> {
                        if (caseCocher.isSelected()) {
                            System.out.println(caseCocher.getText());
                            //carteTab.remettreLigne(caseCocher.getText());
                        } else {
                            //carteTab.enleverLigne(caseCocher.getText());
                        }
                    });

                }
                if(i==1){

                    String nomLivreur = tourCourant.getNomLivreur();
                    Text text = new Text(nomLivreur);
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);

                }else if(i==0 && j==0){
                    Text text = new Text("Tour N°");
                    rectangle.setFill(Color.WHITE);
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);
                }else if(i==2 && j==0){
                    Text text = new Text("Visible sur la map");
                    rectangle.setFill(Color.WHITE);
                    tableau.add(cellule, i, j);
                    cellule.getChildren().add(text);
                }else{
                    tableau.add(cellule, i, j);

                }

            }
        }
        Label titre = new Label("Cliquez sur un numéro pour obtenir les détails du tour");
        titre.setAlignment(Pos.CENTER);
        titre.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
        titre.setTranslateY(-60);
        titre.setTranslateX(-40);

        // Crée un VBox et ajoute le titre et le GridPane
        VBox vbox = new VBox(titre, tableau);
        vbox.setAlignment(Pos.CENTER);

        getChildren().add(vbox);
        setAlignment(Pos.CENTER); // Centre le VBox dans le StackPane


    }


}
