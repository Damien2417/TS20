package main.rootpanels.windows.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Génération des boites de dialogues classique.
 */
public class InformationDialog {
    /**
     * Création de la boite de dialogue.
     * @param title Titre de la boite.
     * @param headerText En-tête de la boite.
     * @param mainText Texte de la boite.
     * @return Retourne la boite de dialogue créée.
     */
    public Dialog makeDialog(String title, String headerText, String mainText) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);


        // Set the button types.
        ButtonType validateButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validateButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label(mainText), 0, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().setStyle("-fx-border-color: #000000; -fx-border-width: 0.5;");
        Optional<Pair<String, String>> result = dialog.showAndWait();
        return dialog;
    }
}
