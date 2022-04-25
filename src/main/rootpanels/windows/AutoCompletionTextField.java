package main.rootpanels.windows;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;


/**
 * Input permettant de sélectionner un joueur
 */
public class AutoCompletionTextField extends TextField {
    //Local variables
    //entries to autocomplete
    private final SortedSet<String> entries;
    //popup GUI
    private ContextMenu entriesPopup;

    /**
     * Crée une instance de suggestion.
     * @param label Conteneur de l'image.
     * @param choix Choix des options.
     */
    public AutoCompletionTextField(Label label, boolean choix) {
        super();
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();
        if(choix) {
            if (PredictionJoueur.idArray[0] < 9) {
                setListener(label, PredictionJoueur.idArray[0], choix);
            }
            PredictionJoueur.idArray[0]++;
        }
        else{
            setListener(label, AjoutTournoi.idArray[0], choix);
            AjoutTournoi.idArray[0]++;
        }
    }


    /**
     * Ajout des listeners sur les inputs.
     * @param label Conteneur de l'image.
     * @param currentId Id de l'input actuel.
     * @param choix Choix des options.
     */
    private void setListener(Label label, int currentId, boolean choix) {
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            showImage(label, "/main/resources/wrong.png", currentId);

            if(choix) {
                PredictionJoueur.checkValide[currentId - 1] = false;
            }
            else{
                AjoutTournoi.checkValide[currentId - 1] = false;
            }
            String enteredText = getText();
            //always hide suggestion if nothing has been entered (only "spacebars" are dissalowed in TextFieldWithLengthLimit)
            if (enteredText == null || enteredText.isEmpty()) {
                entriesPopup.hide();
            } else {
                //filter all possible suggestions depends on "Text", case insensitive
                java.util.List<String> filteredEntries = entries.stream()
                        .filter(e -> e.toLowerCase().contains(enteredText.toLowerCase()))
                        .collect(Collectors.toList());
                //some suggestions are found
                if (!filteredEntries.isEmpty()) {
                    //build popup - list of "CustomMenuItem"
                    populatePopup(filteredEntries, enteredText, label, currentId, choix);
                    if (!entriesPopup.isShowing()) { //optional
                        entriesPopup.show(AutoCompletionTextField.this, Side.BOTTOM, 0, 0); //position of popup
                    }
                    //no suggestions -> hide
                } else {
                    entriesPopup.hide();
                }
            }
        });

        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
        });
    }

    /**
     * Ajoute les suggestions pour l'input.
     * @param searchResult Liste de suggestions correspondantes.
     * @param searchRequest Texte de l'input.
     * @param label Conteneur de l'image.
     * @param currentId Id de l'input actuel.
     * @param choix Choix des options.
     */
    private void populatePopup(List<String> searchResult, String searchRequest, Label label, int currentId, boolean choix) {
        //Liste de suggestions
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //Taille de la liste
        int maxEntries = 4;
        int count = Math.min(searchResult.size(), maxEntries);
        //Liste sous formes de labels
        for (int i = 0; i < count; i++) {
            String result = searchResult.get(i);
            String[] idJoueur=result.split("%");
            searchResult.set(i,idJoueur[0]);
            //Label graphique pour voir la recherche dans les suggestions
            Label entryLabel = new Label();
            entryLabel.setGraphic(buildTextFlow(idJoueur[0], searchRequest));
            entryLabel.setPrefHeight(10);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            //Si une suggestion est sélectionnée, elle est mise dans l'input
            item.setOnAction(actionEvent -> {
                setText(idJoueur[0]);
                positionCaret(idJoueur[0].length());
                entriesPopup.hide();
                showImage(label, "/main/resources/tick.png", currentId);
                if(choix) {
                    PredictionJoueur.checkValide[currentId - 1] = true;
                    PredictionJoueur.idArray[currentId] = Integer.parseInt(idJoueur[1]);
                }
                else{
                    AjoutTournoi.checkValide[currentId - 1] = true;
                    AjoutTournoi.idArray[currentId] = Integer.parseInt(idJoueur[1]);
                }
            });
        }

        //Rafraîchissement
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    /**
     * Récupère la liste des suggestions possibles.
     * @return Retourne la liste des suggestions possibles.
     */
    public SortedSet<String> getEntries() { return entries; }


    /**
     * Surligne la recherche dans la suggestion.
     * @param text Texte à créer.
     * @param filter Mot complet dans la liste de suggestions.
     * @return Retourne le texte mis en forme.
     */
    public static TextFlow buildTextFlow(String text, String filter) {
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
        Text textBefore = new Text(text.substring(0, filterIndex));
        Text textAfter = new Text(text.substring(filterIndex + filter.length()));
        Text textFilter = new Text(text.substring(filterIndex,  filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
        textFilter.setFill(Color.ORANGE);
        textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        return new TextFlow(textBefore, textFilter, textAfter);
    }

    /**
     * Crée l'image du label et l'affiche.
     * @param label Conteneur de l'image.
     * @param path Chemin de l'image à afficher.
     * @param currentId Id de l'input actuel.
     */
    private void showImage(Label label, String path, int currentId){

        Image image = new Image(path);
        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitWidth(12);
        label.setGraphic(imageIcon);

        //position des check
        int[] checkPos = {42,40,60,58,80,78,99,98,89,87,200,197,181,179};
        label.setTranslateY(checkPos[currentId-1]);

        label.setVisible(true);
    }
}