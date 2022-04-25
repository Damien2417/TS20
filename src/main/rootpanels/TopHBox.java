package main.rootpanels;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.connexion.Connexion;
import main.variables.Variables;

/**
 * Barre du haut de l'application
 */
public class TopHBox {
    public static Label nom;
    public static Button deco;
    private static Button minimizeButton;
    private static HBox top = new HBox();

    /**
     * Création de la barre du haut de l'application
     * @param borderPane Panneau principal
     * @return Retourne la barre du haut de l'application
     */
    public HBox createTopHBox(BorderPane borderPane) {
        Image image1 = new Image("/main/resources/logocrop.png");
        ImageView imagelogo = new ImageView(image1);
        imagelogo.setPreserveRatio(true);
        imagelogo.setFitHeight(35);
        top.getStyleClass().add("bg-2");
        top.setPrefHeight(Variables.hauteurTopHBox);
        Label labeltop = new Label();
        labeltop.setPrefHeight(70);
        labeltop.setGraphic(imagelogo);


        Region middleRegion = new Region();
        HBox.setHgrow(middleRegion, Priority.ALWAYS);

        nom = new Label("");
        nom.setTextFill(Color.web("#FFFFFF"));
        nom.setFont(Font.font(Variables.policeIntermediaire, FontWeight.BOLD, Variables.taillePoliceIntermediaire));
        nom.setVisible(true);
        nom.setTranslateY(16);
        nom.setTranslateX(-40);

        deco = new Button("Déconnexion");
        deco.setFont(Font.font(Variables.policeIntermediaire, FontWeight.BOLD, Variables.taillePoliceIntermediaire));
        deco.getStyleClass().add("transparent");
        deco.setStyle("-fx-cursor: hand;");

        deco.setTextFill(Color.web("#FFFFFF"));
        deco.setOnMouseEntered(e-> deco.setTextFill(Color.web("#44576E")));
        deco.setOnMouseExited(e-> deco.setTextFill(Color.web("#FFFFFF")));

        deco.setVisible(false);
        deco.setTranslateY(26);
        deco.setTranslateX(-20);

        deco.setOnMouseClicked(e-> logOut(borderPane));

        minimizeButton = createMinimizeButton(borderPane);
        HBox hboxButtons = new HBox(minimizeButton,createCrossButton(borderPane));

        hboxButtons.setAlignment(Pos.TOP_RIGHT);

        top.getChildren().addAll(labeltop, middleRegion, nom, deco, hboxButtons);

        borderPane.setTop(top);
        return top;
    }

    /**
     * Création du bouton de fermeture de l'application
     * @param borderPane Panneau principal
     * @return Retourne le bouton
     */
    public static Button createCrossButton(BorderPane borderPane) {
        Image image = new Image("/main/resources/cross.png");
        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitHeight(25);
        Image imageHovered = new Image("/main/resources/cross_hover.png");
        ImageView imageHover = new ImageView(imageHovered);
        imageHover.setPreserveRatio(true);
        imageHover.setFitHeight(25);
        Button button = new Button();
        button.setPrefWidth(25);
        button.getStyleClass().add("transparent");
        button.setGraphic(imageIcon);
        button.setOnMouseEntered(e -> button.setGraphic(imageHover));
        button.setOnMouseExited(e -> button.setGraphic(imageIcon));

        button.setOnMouseClicked(e-> exitProgram());
        return button;
    }

    /**
     * Création du bouton de minimisation de l'application
     * @param borderPane Panneau principal
     * @return Retourne le bouton
     */
    public static Button createMinimizeButton(BorderPane borderPane) {
        Image image = new Image("/main/resources/minimize.png");
        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitHeight(25);
        Image imageHovered = new Image("/main/resources/minimize_hover.png");
        ImageView imageHover = new ImageView(imageHovered);
        imageHover.setPreserveRatio(true);
        imageHover.setFitHeight(25);
        Button button = new Button();
        button.setPrefWidth(25);
        button.getStyleClass().add("transparent");
        button.setGraphic(imageIcon);
        button.setOnMouseEntered(e -> button.setGraphic(imageHover));
        button.setOnMouseExited(e -> button.setGraphic(imageIcon));
        button.setOnMouseClicked(e-> minimizeProgram());
        return button;
    }

    /**
     * Minimise l'application
     */
    public static void minimizeProgram() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Déconnexion
     * @param borderPane Panneau principal
     */
    public static void logOut(BorderPane borderPane) {
        Variables.userName="";
        nom.setText("");
        deco.setVisible(false);
        LeftVBox.disconnected(borderPane);
    }

    /**
     * Fermeture de l'application
     */
    private static void exitProgram(){
        Connexion.closeConnection();
        System.exit(1);
    }
}
