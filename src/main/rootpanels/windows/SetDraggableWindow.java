package main.rootpanels.windows;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.rootpanels.MainWindow;

/**
 * Ajout de la fonctionnalité de fenêtre déplaçable.
 */
public class SetDraggableWindow {
    private double xOffset;
    private double yOffset;

    /**
     * Ajout de la fonctionnalité de fenêtre déplaçable.
     * @param primaryStage Stage principal
     */
    public void setDraggable(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow();
        HBox top = mainWindow.returnTopHBox();
        top.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        top.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }
}
