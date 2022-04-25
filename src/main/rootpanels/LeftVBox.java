package main.rootpanels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.rootpanels.windows.*;
import main.rootpanels.windows.dialog.InformationDialog;
import main.variables.Variables;

/**
 * Partie gauche de l'application
 */
public class LeftVBox {
    private static Button buttonIconLogin;
    private static Button buttonIconAddJoueur;
    private static Button buttonIconAddTournoi;
    public static Button buttonIconChart;
    private static Button buttonIconAddress;
    public static VBox left = new VBox();

    /**
     * Création de la page
     * @param borderPane Panneau principal
     */
    public void createLeftVBox(BorderPane borderPane){
        left.getStyleClass().add("bg-3");
        left.setPrefWidth(Variables.largeurLeftVBox);
        buttonIconChart = createButtonChart(borderPane);
        buttonIconAddress = createButtonAddress(borderPane);
        buttonIconLogin = createButtonLogin(borderPane);
        buttonIconAddJoueur = createButtonAddJoueur(borderPane);
        buttonIconAddTournoi = createButtonAddTournoi(borderPane);

        BorderPane.setAlignment(left, Pos.BOTTOM_LEFT);
        left.getChildren().addAll(buttonIconChart, buttonIconAddress, buttonIconLogin);


        borderPane.setLeft(left);
    }

    /**
     * Création du bouton vers la page de prédiction des résultats
     * @param borderPane Panneau principal
     * @return Retourne le bouton
     */
    public Button createButtonChart(BorderPane borderPane) {
        Image image = new Image("/main/resources/chart.png");
        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitHeight(35);
        Image imageHovered = new Image("/main/resources/chart_hover.png");
        ImageView imageHover = new ImageView(imageHovered);
        imageHover.setPreserveRatio(true);
        imageHover.setFitHeight(35);

        Button button = new Button();
        VBox.setMargin(button, new Insets(30, 0, 10, 3));
        button.setPrefWidth(35);
        button.getStyleClass().add("labeliconchart");
        button.setGraphic(imageIcon);
        button.setOnMouseEntered(e -> button.setGraphic(imageHover));
        button.setOnMouseExited(e -> button.setGraphic(imageIcon));
        PredictionJoueur predictionJoueur = new PredictionJoueur();
        button.setOnMouseClicked(e-> predictionJoueur.createPredictionHBox(borderPane));
        return button;
    }

    /**
     * Création du bouton vers l'affichage des joueurs
     * @param borderPane Panneau principal
     * @return Retourne le bouton
     */
    public Button createButtonAddress(BorderPane borderPane) {
        Image image = new Image("/main/resources/address.png");
        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitHeight(35);
        Image imageHovered = new Image("/main/resources/address_hover.png");
        ImageView imageHover = new ImageView(imageHovered);
        imageHover.setPreserveRatio(true);
        imageHover.setFitHeight(35);

        Button button = new Button();
        VBox.setMargin(button, new Insets(10, 0, 10, 5));
        button.setPrefWidth(35);
        button.getStyleClass().add("labeliconchart");
        button.setGraphic(imageIcon);
        button.setOnMouseEntered(e -> button.setGraphic(imageHover));
        button.setOnMouseExited(e -> button.setGraphic(imageIcon));
        PrintJoueur printJoueur = new PrintJoueur();
        button.setOnMouseClicked(e-> {
            printJoueur.createJoueurHBox(borderPane);
        });
        return button;
    }

    /**
     * Création du bouton vers l'ajout des joueurs
     * @param borderPane Panneau principal
     * @return Retourne le bouton
     */
    public Button createButtonAddJoueur(BorderPane borderPane) {
        Image image = new Image("/main/resources/add_joueur.png");
        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitHeight(35);
        Image imageHovered = new Image("/main/resources/add_joueur_hover.png");
        ImageView imageHover = new ImageView(imageHovered);
        imageHover.setPreserveRatio(true);
        imageHover.setFitHeight(35);
        Button button = new Button();
        VBox.setMargin(button, new Insets(10, 0, 10, 0));
        button.setPrefWidth(35);
        button.getStyleClass().add("labeliconaddress");
        button.setGraphic(imageIcon);
        button.setOnMouseEntered(e -> button.setGraphic(imageHover));
        button.setOnMouseExited(e -> button.setGraphic(imageIcon));
        AjoutJoueur createJoueurPage = new AjoutJoueur();
        button.setOnMouseClicked(e-> createJoueurPage.createJoueur(borderPane));
        return button;
    }

    /**
     * Création du bouton vers l'ajout des tournois
     * @param borderPane Panneau principal
     * @return Retourne le bouton
     */
    public Button createButtonAddTournoi(BorderPane borderPane) {
        Image image = new Image("/main/resources/add.png");
        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitHeight(35);
        Image imageHovered = new Image("/main/resources/add_hover.png");
        ImageView imageHover = new ImageView(imageHovered);
        imageHover.setPreserveRatio(true);
        imageHover.setFitHeight(35);
        Button button = new Button();
        VBox.setMargin(button, new Insets(10, 0, 10, 0));
        button.setPrefWidth(35);
        button.getStyleClass().add("labeliconaddress");
        button.setGraphic(imageIcon);
        button.setOnMouseEntered(e -> button.setGraphic(imageHover));
        button.setOnMouseExited(e -> button.setGraphic(imageIcon));
        AjoutTournoi createTournoiPage = new AjoutTournoi();
        button.setOnMouseClicked(e-> createTournoiPage.createAjoutTournoiHBox(borderPane));
        return button;
    }

    /**
     * Création du bouton vers la connexion
     * @param borderPane Panneau principal
     * @return Retourne le bouton
     */
    public Button createButtonLogin(BorderPane borderPane) {
        Image image = new Image("/main/resources/login.png");
        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitHeight(35);
        Image imageHovered = new Image("/main/resources/login_hover.png");
        ImageView imageHover = new ImageView(imageHovered);
        imageHover.setPreserveRatio(true);
        imageHover.setFitHeight(35);
        Button button = new Button();
        VBox.setMargin(button, new Insets(10, 0, 10, 2));
        button.setPrefWidth(35);
        button.getStyleClass().add("labeliconcog");
        button.setGraphic(imageIcon);
        button.setOnMouseEntered(e -> button.setGraphic(imageHover));
        button.setOnMouseExited(e -> button.setGraphic(imageIcon));
        Login createLoginPage = new Login();
        button.setOnMouseClicked(e-> createLoginPage.createLoginHBox(borderPane));
        return button;
    }

    /**
     * Ajout des boutons disponibles pour les administrateurs
     * @param borderPane Panneau principal
     */
    public static void connected(BorderPane borderPane){
        left.getChildren().clear();
        left.getChildren().addAll(buttonIconChart, buttonIconAddTournoi, buttonIconAddress, buttonIconAddJoueur);
        PredictionJoueur predictionJoueur = new PredictionJoueur();
        predictionJoueur.createPredictionHBox(borderPane);
    }

    /**
     * Ajout des boutons disponibles pour les visiteurs
     * @param borderPane Panneau principal
     */
    public static void disconnected(BorderPane borderPane){
        left.getChildren().clear();
        left.getChildren().addAll(buttonIconChart, buttonIconAddress, buttonIconLogin);
        Login createLoginPage = new Login();
        createLoginPage.createLoginHBox(borderPane);
    }
}
