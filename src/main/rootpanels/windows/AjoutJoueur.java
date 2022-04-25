package main.rootpanels.windows;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import main.dao.JoueursDAO;
import main.rootpanels.windows.dialog.InformationDialog;
import main.variables.Variables;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Page permettant de créer un joueur.
 */
public class AjoutJoueur {
    File image;
    boolean imageSelected = false;
    String extension = "";
    Label errorMessage;

    /**
     * Création de la page d'ajout d'un joueur.
     * @param borderPane Panneau principal.
     */
    public void createJoueur(BorderPane borderPane) {
        HBox center = new HBox();
        center.getStyleClass().add("bg-4");
        BorderPane.setAlignment(center, Pos.TOP_CENTER);

        VBox leftComposant = null;
        leftComposant = createLeftComposant();
        VBox rightComposant = createRightComposant(borderPane);

        HBox.setHgrow(leftComposant, Priority.ALWAYS);
        HBox.setHgrow(rightComposant, Priority.NEVER);
        center.getChildren().addAll(leftComposant,rightComposant);
        center.setAlignment(Pos.CENTER);
        borderPane.setCenter(center);
    }

    /**
     * Crée le composant de gauche de la page
     * @return Retourne le composant de gauche de la page.
     */
    private VBox createLeftComposant() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(0, 0, 0, 0));

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        VBox vboxFinal = new VBox();
        vboxFinal.setAlignment(Pos.BASELINE_RIGHT);

        errorMessage = new Label();
        errorMessage.setFont(Font.font(Variables.policeBasique, FontWeight.NORMAL, Variables.taillePoliceBasique));
        errorMessage.setTextFill(Color.web("#dc3545"));
        errorMessage.setWrapText(true);
        errorMessage.setPrefWidth(150);
        errorMessage.setVisible(false);
        errorMessage.setTextAlignment(TextAlignment.CENTER);

        HBox errorMessageHBox = new HBox();
        errorMessageHBox.setAlignment(Pos.TOP_RIGHT);
        errorMessageHBox.getChildren().add(errorMessage);

        Text title = new Text("Ajouter un joueur");
        title.setFont(Font.font(Variables.policeTitre, FontWeight.NORMAL, Variables.taillePoliceTitre));


        Label lastName = new Label("Nom");
        lastName.setFont(Font.font(Variables.policeBasique, FontWeight.NORMAL, Variables.taillePoliceBasique));

        TextField lastNameTextField = new TextField();
        lastNameTextField.setMaxWidth(200);

        HBox hboxLastName = new HBox();
        hboxLastName.setAlignment(Pos.BASELINE_RIGHT);
        VBox.setMargin(hboxLastName, new Insets(20, 0, 0, 0));
        hboxLastName.getChildren().addAll(lastName,lastNameTextField);


        Label name = new Label("Prénom");
        name.setFont(Font.font(Variables.policeBasique, FontWeight.NORMAL, Variables.taillePoliceBasique));

        TextField nameTextField = new TextField();
        nameTextField.setMaxWidth(200);

        HBox hboxName = new HBox();
        hboxName.setAlignment(Pos.BASELINE_RIGHT);
        VBox.setMargin(hboxName, new Insets(10, 0, 0, 0));
        hboxName.getChildren().addAll(name,nameTextField);


        Label club = new Label("Club");
        club.setFont(Font.font(Variables.policeBasique, FontWeight.NORMAL, Variables.taillePoliceBasique));

        TextField clubTextField = new TextField();
        clubTextField.setMaxWidth(200);


        Button validateButton = new Button("Valider");
        validateButton.getStyleClass().add("btn-primary");
        VBox.setMargin(validateButton, new Insets(20, 48, 0, 0));
        validateButton.setAlignment(Pos.CENTER);
        validateButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                errorMessage.setVisible(false);
                if(lastNameTextField.getText().length() > 0 & nameTextField.getText().length() > 0 & clubTextField.getText().length() > 0) {

                    Dialog<Pair<String, String>> dialog = new Dialog<>();
                    dialog.setTitle("Avertissement");
                    dialog.setHeaderText("A propos de l'ajout d'un joueur");


                    // Set the button types.
                    ButtonType validateButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(validateButtonType,ButtonType.CANCEL);

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    grid.add(new Label("Veuillez ne pas ajouter une photo d'une personne non-consentante ou mineure.\nVeillez aussi à utiliser la photo officielle, sur laquelle n'est visible que le joueur.\nNous ne serons pas tenus responsables en cas de litige."), 0, 0);

                    dialog.getDialogPane().setContent(grid);
                    dialog.initStyle(StageStyle.UNDECORATED);
                    dialog.getDialogPane().setStyle("-fx-border-color: #000000; -fx-border-width: 0.5;");

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == validateButtonType){
                            dialog.close();
                            JoueursDAO joueursDAO = new JoueursDAO();
                            if(joueursDAO.sendJoueur(lastNameTextField.getText(), nameTextField.getText(), clubTextField.getText(), errorMessage, extension, imageSelected, image)){
                                InformationDialog informationDialog = new InformationDialog();
                                informationDialog.makeDialog("Ajout", "Ajout de joueur", "Joueur ajouté avec succès");
                            }
                            lastNameTextField.setText("");
                            nameTextField.setText("");
                            clubTextField.setText("");
                        }
                        return null;
                    });
                    dialog.showAndWait();

                }
                else {
                    errorMessage.setText("Veuillez remplir tous les champs");
                    errorMessage.setVisible(true);
                }
            }
        });

        HBox hboxClub = new HBox();
        hboxClub.setAlignment(Pos.BASELINE_RIGHT);
        VBox.setMargin(hboxClub, new Insets(10, 0, 0, 0));
        hboxClub.getChildren().addAll(club,clubTextField);

        hbox.getChildren().addAll(vboxFinal);
        vbox.getChildren().add(hbox);

        vboxFinal.getChildren().addAll(errorMessageHBox, title, hboxLastName, hboxName, hboxClub, validateButton);

        return vbox;
    }

    /**
     * Crée le composant de droite de la page
     * @param borderPane Panneau principal.
     * @return Retourne le composant de droite de la page.
     */
    private VBox createRightComposant(BorderPane borderPane) {
        VBox vbox = new VBox();

        vbox.setAlignment(Pos.CENTER_RIGHT);

        Image image = new Image("/main/resources/upload.png");

        ImageView imageIcon = new ImageView(image);
        imageIcon.setPreserveRatio(true);
        imageIcon.setFitHeight(35);
        Button button = new Button("Parcourir", imageIcon);
        button.setContentDisplay(ContentDisplay.TOP);
        button.getStyleClass().add("transparent");
        button.setFont(Font.font(Variables.policeBasique, FontWeight.NORMAL, Variables.taillePoliceBasique));
        VBox.setMargin(button,new Insets(0, 100, 0, 0));

        button.setOnMouseClicked(e-> imageChooser(vbox, button, borderPane));


        vbox.getChildren().add(button);

        return vbox;
    }

    /**
     * Crée une boite de dialogue pour choisir une image.
     * @param vbox Conteneur.
     * @param button Bouton de selection du fichier.
     * @param borderPane Panneau principal.
     */
    private void imageChooser(VBox vbox, Button button, BorderPane borderPane)
    {
        FileChooser fileChooser = new FileChooser();
        //Show open file dialog
        image = fileChooser.showOpenDialog(borderPane.getScene().getWindow());

        Image img = null;
        if (image != null) {
            try {
                extension = "";
                extension = image.getName().substring(image.getName().lastIndexOf('.') + 1);
                BufferedImage bufferedImage = ImageIO.read(image);
                img = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageView imageJoueur = new ImageView(img);
                imageJoueur.setPreserveRatio(true);
                imageJoueur.setFitWidth((int) (Variables.largeurCenter * 0.4)+1);
                vbox.getChildren().removeAll(button);
                vbox.getChildren().add(imageJoueur);
                imageSelected = true;
            } catch (IOException e) {
                InformationDialog informationDialog = new InformationDialog();
                informationDialog.makeDialog("Erreur image", "Une erreur est survenue lors du choix de l'image", "Une erreur est survenue lors du choix de l'image. Veuillez verifier le format de l'image.\n" + e);
            }
        }

    }
}