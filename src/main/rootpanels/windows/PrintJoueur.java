package main.rootpanels.windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import main.connexion.Connexion;
import main.connexion.ConnexionFTP;
import main.dao.JoueursDAO;
import main.rootpanels.windows.dialog.InformationDialog;
import main.variables.Variables;

import java.util.Optional;

/**
 * Page permettant d'afficher la liste des joueurs.
 */
public class PrintJoueur {
    int currentPage=1;
    HBox hboxPageNumber;
    ConnexionFTP connexionFTP;
    JoueursDAO getNbJoueur = new JoueursDAO();
    double count;
    int countInt;
    {
        count = (getNbJoueur.getNbJoueur()/8)+ 0.875;
        countInt = (int) count;
    }
    String[][] array;
    VBox mainVBox;
    String search="";

    /**
     * Crée la HBox où les joueurs seront insérés.
     * @param borderPane Panneau principal.
     */
    public void createJoueurHBox(BorderPane borderPane) {
        HBox center = new HBox();
        center.getStyleClass().add("bg-4");
        connexionFTP = null;
        connexionFTP = new ConnexionFTP();
        JoueursDAO joueursDAO = new JoueursDAO();
        array = joueursDAO.getJoueursPage("SELECT id,nom,prenom,photo FROM joueurs LIMIT 0, 8");
        center.setAlignment(Pos.CENTER);
        mainVBox = new VBox();
        mainVBox.prefWidthProperty().bind(center.widthProperty());
        mainVBox.setAlignment(Pos.CENTER);
        hboxPageNumber = createPageNumber();
        mainVBox.getChildren().addAll(createSearch(), createAffichageJoueur(0),createAffichageJoueur(4), hboxPageNumber);
        center.getChildren().add(mainVBox);
        borderPane.setCenter(center);
    }

    /**
     * Crée une ligne de joueur à afficher.
     * @param increment Début de la plage de joueur à ajouter.
     * @return Retourne la ligne de joueur.
     */
    private HBox createAffichageJoueur(int increment) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(20, 0, 0, 0));
        for(int i=0+increment;i<4+increment;i++) {
            if(array[i][0]!=null){
                makeAffichage(hbox,Integer.parseInt(array[i][0]),array[i][1],array[i][2],array[i][3]);
            }
        }
        return hbox;
    }

    /**
     * Crée l'affichage d'un joueur.
     * @param hboxF Hbox de la ligne où se situe le joueur.
     * @param id Id du joueur.
     * @param nom Nom du joueur.
     * @param prenom Prenom du joueur.
     * @param extension Extension de l'image.
     */
    private void makeAffichage(HBox hboxF, int id, String nom, String prenom, String extension){
        ImageView imageView;
        Image image;
        if (extension.length() > 0) {
            try {
                image = connexionFTP.retrieveImage("/www/imagesJoueursProjetJava/" + id + "." + extension);
                imageView = new ImageView(image);
            }
            catch (NullPointerException e){
                image = new Image("/main/resources/default-profile.png");
                imageView = new ImageView(image);
            }
        } else {
            image = new Image("/main/resources/default-profile.png");
            imageView = new ImageView(image);
        }
        if(imageView==null){
            image = new Image("/main/resources/default-profile.png");
            imageView = new ImageView(image);
        }

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(165);
        if(image.getHeight() > image.getWidth()){
            imageView.setFitHeight(165);
        }
        Button button = new Button(nom + " " + prenom , imageView);
        button.setContentDisplay(ContentDisplay.TOP);
        button.getStyleClass().addAll("transparent", "card");
        button.setPrefWidth(165);
        button.setFont(Font.font(Variables.policeBasique, FontWeight.NORMAL, Variables.taillePoliceBasique));
        hboxF.setAlignment(Pos.BOTTOM_CENTER);
        HBox.setMargin(button,new Insets(0, 27, 0, 27));
        button.setOnAction(event -> {
            getJoueurSpecificities(nom, prenom, id);
        });

        hboxF.getChildren().add(button);
    }

    /**
     * Crée la sélection des pages.
     * @return Retourne la sélection des pages.
     */
    private HBox createPageNumber() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        VBox.setMargin(hbox, new Insets(35,0,0,0));
        hbox.getChildren().add(createPageButton(1));
        //Gère l'affichage selon le nombre de pages et la page actuelle
        if(currentPage-3 > 1 && currentPage +3 < countInt){
            if(currentPage-3 != 2){
                hbox.getChildren().add(createPointsSuspension());
            }
            for(int i=currentPage-3;i<=currentPage+3;i++){
                hbox.getChildren().add(createPageButton((int) i));
            }
            if(currentPage+3 != countInt-1){
                hbox.getChildren().add(createPointsSuspension());
            }
        }else if(currentPage-3 > 1){
            if(currentPage-3 != 2){
                hbox.getChildren().add(createPointsSuspension());
            }
            for(int i=currentPage-3;i<countInt;i++){
                hbox.getChildren().add(createPageButton(i));
            }
        }else if(currentPage +3 < (int) count){
            for(int i=2;i<=currentPage+3;i++){
                hbox.getChildren().add(createPageButton(i));
            }
            if(currentPage+3 != countInt-1){
                hbox.getChildren().add(createPointsSuspension());
            }
        }else{
            for(int i=2;i<countInt;i++){
                hbox.getChildren().add(createPageButton(i));
            }
        }
        if(count>=2){
            hbox.getChildren().add(createPageButton(countInt));
        }
        return hbox;
    }

    /**
     * Crée la sélection des pages selon le résultat.
     * @param countIntSearch Nombre de pages.
     * @return Retourne la sélection des pages selon le résultat.
     */
    private HBox createPageNumberSearch(int countIntSearch) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        VBox.setMargin(hbox, new Insets(35,0,0,0));
        hbox.getChildren().add(createPageButton(1));
        //Gère l'affichage selon le nombre de pages et la page actuelle
        if(currentPage-3 > 1 && currentPage +3 < countIntSearch){
            if(currentPage-3 != 2){
                hbox.getChildren().add(createPointsSuspension());
            }
            for(int i=currentPage-3;i<=currentPage+3;i++){
                hbox.getChildren().add(createPageButton((int) i));
            }
            if(currentPage+3 != countIntSearch-1){
                hbox.getChildren().add(createPointsSuspension());
            }
        }else if(currentPage-3 > 1){
            if(currentPage-3 != 2){
                hbox.getChildren().add(createPointsSuspension());
            }
            for(int i=currentPage-3;i<countIntSearch;i++){
                hbox.getChildren().add(createPageButton(i));
            }
        }else if(currentPage +3 < countIntSearch){
            for(int i=2;i<=currentPage+3;i++){
                hbox.getChildren().add(createPageButton(i));
            }
            if(currentPage+3 != countIntSearch-1){
                hbox.getChildren().add(createPointsSuspension());
            }
        }else{
            for(int i=2;i<countIntSearch;i++){
                hbox.getChildren().add(createPageButton(i));
            }
        }
        if(countIntSearch>=2){
            hbox.getChildren().add(createPageButton(countIntSearch));
        }
        return hbox;
    }

    /**
     * Crée le bouton de changement de page.
     * @param nb Numéro de la page.
     * @return Retourne le bouton.
     */
    private Button createPageButton(int nb){
        Button button = new Button(String.valueOf(nb));
        button.getStyleClass().add("page-button");
        if(nb==currentPage){
            button.setStyle("-fx-font-weight: bold;");
        }
        EventHandler<ActionEvent> actionEventEventHandler = event -> {
            goToPage(nb);
        };
        button.setOnAction(actionEventEventHandler);
        return button;
    }

    /**
     * Crée les points de suspensions à afficher.
     * @return Retourne les points de suspensions à afficher.
     */
    private Button createPointsSuspension(){
        Button button = new Button("...");
        button.getStyleClass().add("transparent-padding");
        return button;
    }

    /**
     * Crée la page à l'index voulu.
     * @param nb Numéro de la page.
     */
    private void goToPage(int nb) {
        currentPage=nb;
        nb=nb*8-8;
        JoueursDAO joueursDAO = new JoueursDAO();
        array = joueursDAO.getJoueursPage("SELECT id,nom,prenom,photo FROM joueurs"+search+" LIMIT " + nb + ", 8");
        if(search=="") {
            hboxPageNumber = createPageNumber();
        }else{
            double count = joueursDAO.getNbJoueurSearch(search);
            hboxPageNumber = createPageNumberSearch((int) count);
        }
        mainVBox.getChildren().clear();
        mainVBox.getChildren().addAll(createSearch(), createAffichageJoueur(0),createAffichageJoueur(4), hboxPageNumber);
    }

    /**
     * Crée et affiche les informations d'un joueur.
     * @param nom Nom du joueur.
     * @param prenom Prenom du joueur.
     * @param id Id du joueur.
     */
    private void getJoueurSpecificities(String nom,String prenom, int id) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(nom + " " + prenom);
        dialog.setHeaderText("Modification joueur");
        //dialog.initStyle(StageStyle.UNIFIED);

        //Input et label de la fenetre
        TextField nomInput = new TextField();
        TextField prenomInput = new TextField();
        nomInput.setText(nom);
        prenomInput.setText(prenom);

        // Set the button types.
        ButtonType validateButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);
        if(Variables.userName!=""){
            dialog.getDialogPane().getButtonTypes().addAll(validateButtonType, deleteButtonType,ButtonType.CANCEL);
        }
        else{
            nomInput.setDisable(true);
            nomInput.setStyle("-fx-opacity: 0.8;");
            prenomInput.setDisable(true);
            prenomInput.setStyle("-fx-opacity: 0.8;");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        }

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomInput, 1, 0);
        grid.add(new Label("Prenom:"), 0, 1);
        grid.add(prenomInput, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validateButtonType) {
                try {
                    int res = Connexion.requeteUpdate("UPDATE joueurs SET nom = '"+nomInput.getText()+"', prenom = '"+prenomInput.getText()+"' WHERE id="+id);
                    if (res > 0) {
                        InformationDialog informationDialog = new InformationDialog();
                        informationDialog.makeDialog("Modification", "Modification de joueur", "Joueur modifié avec succès");
                    }
                    dialog.close();
                } catch (Exception e) {
                    InformationDialog informationDialog = new InformationDialog();
                    informationDialog.makeDialog("Modification", "Modification de joueur", "Le joueur n'a pas pus être modifié.\n" + e);
                    dialog.close();
                    return null;
                }
            }
            else if(dialogButton == deleteButtonType) {
                Dialog<Pair<String, String>> deleteDialog = new Dialog<>();
                deleteDialog.setTitle(nom + " " + prenom);
                deleteDialog.setHeaderText("Modification joueur");
                deleteDialog.initStyle(StageStyle.UTILITY);


                // Set the button types.
                ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
                deleteDialog.getDialogPane().getButtonTypes().addAll(yesButton, noButton);

                GridPane gridDeleteDialog = new GridPane();
                gridDeleteDialog.setHgap(10);
                gridDeleteDialog.setVgap(10);
                gridDeleteDialog.setPadding(new Insets(20, 150, 10, 10));
                gridDeleteDialog.add(new Label("Voulez-vous vraiment supprimer ce joueur ?"), 0, 0);
                deleteDialog.getDialogPane().setContent(gridDeleteDialog);

                deleteDialog.setResultConverter(dialogDeleteButton -> {
                    if (dialogDeleteButton == yesButton) {
                        try {
                            int res = Connexion.requeteUpdate("DELETE FROM joueurs WHERE id='" + id + "'");
                            if (res > 0) {
                                InformationDialog informationDialog = new InformationDialog();
                                informationDialog.makeDialog("Modification", "Modification de joueur", "Joueur supprimé avec succès");
                            }
                            deleteDialog.close();
                            goToPage(currentPage);
                            return null;
                        } catch (Exception e) {
                            InformationDialog informationDialog = new InformationDialog();
                            informationDialog.makeDialog("Modification", "Modification de joueur", "Le joueur n'a pas pus être supprimé.\n" + e);
                            deleteDialog.close();
                            return null;
                        }
                    }
                    if (dialogDeleteButton == noButton) {
                        deleteDialog.close();
                    }
                    return null;
                });
                Optional<Pair<String, String>> result = deleteDialog.showAndWait();
            }
            dialog.close();
            return null;
        });

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/main/resources/icon.png").toString()));
        stage.showAndWait();

    }

    /**
     * Création de la recherche de joueurs.
     * @return Retourne le conteneur.
     */
    private HBox createSearch() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(0, 20, 0, 0));
        hbox.setAlignment(Pos.CENTER_RIGHT);
        TextField textField = new TextField();
        HBox.setMargin(textField, new Insets(0, 10, 0, 0));
        Button button = new Button("Rechercher");
        HBox.setMargin(button, new Insets(0, 10, 0, 0));
        button.getStyleClass().add("btn-primary");

        EventHandler<ActionEvent> actionEventEventHandler = event -> {
            String searchText = "'%"+ textField.getText()+"%'";
            search=" WHERE prenom LIKE "+searchText+" OR nom LIKE "+searchText+" ";
            goToPage(1);
        };
        button.setOnAction(actionEventEventHandler);

        textField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                button.fire();
                ev.consume();
            }
        });

        hbox.getChildren().addAll(textField,button);
        return hbox;
    }

}