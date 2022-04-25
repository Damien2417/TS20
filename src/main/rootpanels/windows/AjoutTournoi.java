package main.rootpanels.windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import main.dao.JoueursDAO;
import main.dao.MatchsDAO;
import main.rootpanels.windows.dialog.InformationDialog;
import main.variables.Variables;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Page permettant d'ajouter un tournoi
 */
public class AjoutTournoi {
    static boolean[] checkValide;

    List<Label> listCheckJoueurs;
    List<Button> listButtonSets;
    List<AutoCompletionTextField> listJoueursInput;

    int[][] tabSets = new int[7][10];

    ArrayList<String> listJoueurs;
    public static Integer[] idArray = new Integer[15];

    /**
     * Création de la page
     * @param borderPane Panneau principal
     */
    public void createAjoutTournoiHBox(BorderPane borderPane){
        checkValide = new boolean[14];
        listCheckJoueurs = new ArrayList<>();
        listButtonSets = new ArrayList<>();
        listJoueursInput = new ArrayList<>();
        listJoueurs=new ArrayList<>();
        HBox center = new HBox();
        center.getStyleClass().add("bg-4");
        BorderPane.setAlignment(center, Pos.TOP_CENTER);

        VBox content = new VBox();

        idArray[0]=1;

        HBox topPart = new HBox();
        VBox.setMargin(topPart,new Insets(30, 0, 0, 0));

        topPart.setMinHeight(450);
        BackgroundImage tournoiImage= new BackgroundImage(new Image("/main/resources/ajoutTournois.png",665,395,false,false),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        topPart.setBackground(new Background(tournoiImage));

        for(int i=1;i<15;i++){
            listCheckJoueurs.add(new Label());
        }

        //Vbox réglages set quart de final
        VBox boutonSetQuarter = new VBox();
        boutonSetQuarter.setMinWidth(35);
        boutonSetQuarter.setMaxWidth(35);
        int[][] buttonSetPosQuarter = {{0,62},{0,135}, {0,203},{0,270}};
        for(int i=0;i<4;i++){
            listButtonSets.add(makeButtonSet(buttonSetPosQuarter[i][0],buttonSetPosQuarter[i][1]));
        }

        boutonSetQuarter.getChildren().addAll(listButtonSets);

        //Vbox check right wrong + results
        VBox vBoxCheckQuarter = new VBox();
        vBoxCheckQuarter.setMinWidth(35);
        vBoxCheckQuarter.setMaxWidth(35);
        HBox.setMargin(vBoxCheckQuarter,new Insets(0,0,0,17));

        //Vbox joueurs input quart de final
        VBox vBoxJoueurQuarter = new VBox();
        vBoxJoueurQuarter.setPadding(new Insets(0, 14, 0, 0));

        //Création des labels
        int[] inputCoords = {45,50,77,81,108,112,140,144,92,96,218,222,185,189};

        for(int i=0;i<inputCoords.length;i++){
            listJoueursInput.add(makeInputJoueur(inputCoords[i], listCheckJoueurs.get(i)));
        }

        //Vbox réglages set semifinal
        VBox boutonSetSemi = new VBox();
        boutonSetSemi.setMinWidth(35);
        boutonSetSemi.setMaxWidth(35);
        HBox.setMargin(boutonSetSemi,new Insets(0,0,0,15));
        int[][] buttonSetPosSemi = {{-5,113},{-5,275}};
        for(int i=0;i<2;i++){
            listButtonSets.add(makeButtonSet(buttonSetPosSemi[i][0],buttonSetPosSemi[i][1]));
            boutonSetSemi.getChildren().add(listButtonSets.get(i+4));
        }


        //Vbox check semifinal
        VBox vBoxCheckSemi = new VBox();
        vBoxCheckSemi.setMinWidth(35);
        vBoxCheckSemi.setMaxWidth(35);
        vBoxCheckSemi.setPadding(new Insets(0, 0, 0, 0));
        HBox.setMargin(vBoxCheckSemi,new Insets(0,0,0,0));

        //Vbox joueurs input semi final
        VBox vBoxJoueurSemi = new VBox();
        vBoxJoueurSemi.setPadding(new Insets(0, 14, 0, 0));

        //Vbox check final
        VBox vBoxCheckFinal = new VBox();
        vBoxCheckFinal.setMinWidth(35);
        vBoxCheckFinal.setMaxWidth(35);
        vBoxCheckFinal.setPadding(new Insets(0, 0, 0, 0));
        HBox.setMargin(vBoxCheckFinal,new Insets(0,0,0,0));

        //Vbox joueurs input final
        VBox vBoxJoueurFinal = new VBox();
        vBoxJoueurFinal.setPadding(new Insets(0, 14, 0, 0));

        //Vbox réglages set final
        VBox boutonSetFinal = new VBox();
        boutonSetFinal.setMinWidth(35);
        boutonSetFinal.setMaxWidth(35);
        HBox.setMargin(boutonSetFinal,new Insets(0,0,0,20));

        listButtonSets.add(makeButtonSet(-15,207));
        boutonSetFinal.getChildren().add(listButtonSets.get(6));

        for(int i=0;i<8;i++){
            vBoxCheckQuarter.getChildren().add(listCheckJoueurs.get(i));
        }

        for(int i=0;i<8;i++){
            vBoxJoueurQuarter.getChildren().add(listJoueursInput.get(i));
        }

        for(int i=8;i<12;i++){
            vBoxCheckSemi.getChildren().add(listCheckJoueurs.get(i));
        }
        vBoxJoueurSemi.getChildren().addAll(listJoueursInput.get(8),listJoueursInput.get(9),listJoueursInput.get(10),listJoueursInput.get(11));

        vBoxCheckFinal.getChildren().addAll(listCheckJoueurs.get(12),listCheckJoueurs.get(13));
        vBoxJoueurFinal.getChildren().addAll(listJoueursInput.get(12),listJoueursInput.get(13));

        topPart.getChildren().addAll(boutonSetQuarter,vBoxCheckQuarter,vBoxJoueurQuarter,boutonSetSemi,vBoxCheckSemi,vBoxJoueurSemi,vBoxCheckFinal,vBoxJoueurFinal, boutonSetFinal);

        HBox actionBar = new HBox();
        Button validateButton = new Button("Ajouter le tournoi");
        validateButton.getStyleClass().add("btn-primary");
        validateButton.setAlignment(Pos.CENTER);
        validateButton.setOnMouseClicked(e-> {
            if(!Arrays.asList(checkValide).contains(false)) {

                // Create the custom dialog.
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Tournoi");
                dialog.setHeaderText("Envoi du tournoi");

                // Set the button types.
                ButtonType validateButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(validateButtonType,ButtonType.CANCEL);

                VBox vBox= new VBox();
                Label texte = new Label("Voulez-vous vraiment ajouter ce tournoi ?\n");
                texte.setFont(Font.font(Variables.policeBasique));

                vBox.getChildren().add(texte);
                vBox.setPadding(new Insets(20, 20, 20, 20));

                dialog.getDialogPane().setContent(vBox);
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == validateButtonType) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        String dateJour = dateFormat.format(date);
                        MatchsDAO matchsDAO = new MatchsDAO();
                        int maxTournoi = matchsDAO.getMaxTournoi(dialog);
                        for(int i=0;i<7;i++){
                            int idJoueur1 = idArray[i*2+1];
                            int idJoueur2 = idArray[i*2+2];
                            int vainqueur;
                            int joueur1Set = 0, joueur2Set = 0;
                            String jeux = "";
                            for(int y=0;y<5;y++){
                                if(tabSets[i][y*2] > tabSets[i][y*2+1]){
                                    joueur1Set++;
                                    jeux+=tabSets[i][y*2]+","+tabSets[i][y*2+1];
                                }else if(tabSets[i][y*2] < tabSets[i][y*2+1]){
                                    joueur2Set++;
                                    jeux+=tabSets[i][y*2]+","+tabSets[i][y*2+1];
                                }else{
                                    jeux+=",";
                                }
                                if(y<4){
                                    jeux+=",";
                                }
                            }
                            if(joueur2Set > joueur1Set){
                                vainqueur=idJoueur2;
                            }else{
                                vainqueur=idJoueur1;
                            }
                            matchsDAO.insertMatch((maxTournoi+1)+"', '"+idJoueur1+"', '"+idJoueur2+"', '"+vainqueur+"', '"+joueur1Set+"-"+joueur2Set+"', '"+jeux+"', '"+dateJour);
                        }
                        InformationDialog informationDialog = new InformationDialog();
                        informationDialog.makeDialog("Tournoi", "Ajout de tournoi", "Le tournoi a bien été ajouté.");
                }
                    dialog.close();
                    return null;
                });
                dialog.showAndWait();
            }
            else{
                InformationDialog informationDialog = new InformationDialog();
                informationDialog.makeDialog("Ajout d'un tournoi", "L'ajout n'a pas pu être fait", "Vous ne pouvez pas ajouter un tournoi sans avoir validé tous les champs.");
            }
        });
        actionBar.setMinWidth(700);
        actionBar.setAlignment(Pos.CENTER);

        actionBar.getChildren().addAll(validateButton);

        content.getChildren().addAll(topPart,actionBar);

        center.getChildren().addAll(content);
        center.setAlignment(Pos.CENTER);
        borderPane.setCenter(center);

        //requete get joueurs
        JoueursDAO joueursDAO = new JoueursDAO();
        joueursDAO.getJoueurs(listJoueurs);

        for(int i=0;i<14;i++){
            inputAddAutoCompletion(listJoueursInput.get(i));
        }
    }

    /**
     * Création des inputs
     * @param y Position vertical
     * @param label Conteneur
     * @return Retourne l'input
     */
    private AutoCompletionTextField makeInputJoueur(int y, Label label){
        AutoCompletionTextField actualJoueur = new AutoCompletionTextField(label, false);
        actualJoueur.setMaxWidth(120);
        actualJoueur.setMinHeight(31);
        actualJoueur.getStyleClass().add("inputtransparent");
        actualJoueur.setTranslateY(y);
        return actualJoueur;
    }

    /**
     * Création de la fenêtre d'ajout des sets
     * @param index Numéro des inputs sélectionnés
     */
    private void createSetsWindow(int index){
        String nomJ1=listJoueursInput.get(index*2).getText();
        String nomJ2=listJoueursInput.get(index*2+1).getText();

        if(!checkValide[index *2]){
            nomJ1="Joueur 1";
        }
        if(!checkValide[index *2+1]){
            nomJ2="Joueur 2";
        }

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Sets");
        dialog.setHeaderText("Ajout des sets pour "+nomJ1+" contre "+nomJ2);

        // Set the button types.
        ButtonType validateButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validateButtonType,ButtonType.CANCEL);

        VBox vBox= new VBox();
        Label texte = new Label("Veuillez saisir les sets pour ce match\n");
        texte.setFont(Font.font(Variables.policeBasique));
        Label precision = new Label("Si un set n'a pas eu lieu, veuillez laisser '0-0'\n");
        precision.setFont(Font.font(Variables.policeBasique));

        precision.setStyle("-fx-font-size: 7pt;");

        vBox.getChildren().addAll(texte,precision);
        int[] tabSetTemp = new int[10];

        //input sets
        for(int i=0;i<5;i++){
            final int ind = i*2;

            HBox newLine = new HBox();
            newLine.setAlignment(Pos.BASELINE_CENTER);
            VBox.setMargin(newLine, new Insets(10, 0, 0, 0));
            TextField setJ1 = new TextField(tabSets[index][ind]+"");
            setJ1.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[0-7]{0,1}?")) {
                    setJ1.setText(oldValue);
                }
                try {
                    tabSetTemp[ind] = Integer.parseInt(setJ1.getText());
                }
                catch(Exception e){
                    tabSetTemp[ind] = 0;
                }
            });


            setJ1.setMinWidth(35);
            setJ1.setMaxWidth(35);
            TextField setJ2 = new TextField(tabSets[index][ind+1]+"");

            setJ2.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[0-7]{0,1}?")) {
                    setJ2.setText(oldValue);
                }
                try {
                    tabSetTemp[ind+1] = Integer.parseInt(setJ2.getText());
                }
                catch(Exception e){
                    tabSetTemp[ind+1] = 0;
                }

            });

            setJ2.setMinWidth(35);
            setJ2.setMaxWidth(35);

            Label set1 = new Label("set "+(i+1)+" ");
            set1.setFont(Font.font(Variables.policeBasique));
            Label tiret = new Label("-");
            tiret.setFont(Font.font(Variables.policeBasique));

            newLine.getChildren().addAll(set1,setJ1,tiret,setJ2);
            vBox.getChildren().add(newLine);
        }


        vBox.setPadding(new Insets(20, 20, 20, 20));

        dialog.getDialogPane().setContent(vBox);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validateButtonType) {
                try {
                    tabSets[index]=tabSetTemp;
                    dialog.close();
                } catch (Exception e) {
                    InformationDialog informationDialog = new InformationDialog();
                    informationDialog.makeDialog("Modification", "Modification de set", "Le set n'a pas pus être modifié.\n" + e);
                    dialog.close();
                    return null;
                }
            }
            dialog.close();
            return null;
        });

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/main/resources/icon.png").toString()));
        stage.showAndWait();

    }

    /**
     * Création du bouton ouvrant la fenêtre d'ajout des sets
     * @param x Position horizontale
     * @param y Position verticale
     * @return Retourne le bouton
     */
    private Button makeButtonSet(int x, int y){
        ImageView imageSet = new ImageView(new Image("/main/resources/tennis.png"));
        imageSet.setPreserveRatio(true);
        imageSet.setFitHeight(17);
        Button buttonSet = new Button("",imageSet);
        buttonSet.setTranslateX(x);
        buttonSet.setTranslateY(y);

        buttonSet.setOnMouseClicked(e-> {
            createSetsWindow(listButtonSets.indexOf(buttonSet));
        });

        return buttonSet;
    }

    /**
     * Ajout de la liste des joueurs dans les inputs
     * @param textField Input de sélection d'un joueur
     */
    private void inputAddAutoCompletion(AutoCompletionTextField textField){
        textField.getText();
        textField.getEntries().addAll(listJoueurs);
    }
}