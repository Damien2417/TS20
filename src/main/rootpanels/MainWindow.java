package main.rootpanels;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Panneau principal
 */
public class MainWindow {
    private static HBox top;
    public static BorderPane borderPane;

    /**
     * Création du panneau principal
     * @return Retourne le panneau principal
     */
    public BorderPane createMainBP() {
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(20,20,20,20));
        container.setStyle("-fx-background-color: transparent;-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.5, 0.0, 0.0);");
        borderPane = new BorderPane();
        borderPane.getStyleClass().add("bg-1");

        //Make topHBox
        TopHBox topHBox = new TopHBox();
        top = topHBox.createTopHBox(borderPane);

        //Make leftVBox
        LeftVBox createLeftVBox = new LeftVBox();
        createLeftVBox.createLeftVBox(borderPane);

        container.setCenter(borderPane);
        return container;
    }

    /**
     * Récupère la barre du haut de l'application
     * @return Retourne la barre du haut de l'application
     */
    public HBox returnTopHBox() {
        return top;
    }

}