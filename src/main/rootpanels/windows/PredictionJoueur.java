package main.rootpanels.windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import main.dao.JoueursDAO;
import main.dao.MatchsDAO;
import main.rootpanels.windows.dialog.InformationDialog;
import java.util.*;


/**
 * Page de prédiction des résultats
 */
public class PredictionJoueur {
    static boolean[] checkValide;
    int[][] points;

    List<Label> listCheckJoueurs;
    List<AutoCompletionTextField> listJoueursInput;

    ArrayList<String> listJoueurs;
    public static Integer[] idArray = new Integer[9];

    /***
     * Création de la page
     * @param borderPane Panneau principal
     */
    public void createPredictionHBox(BorderPane borderPane){
        checkValide =  new boolean[8];
        listCheckJoueurs = new ArrayList<>();
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
        BackgroundImage tournoiImage= new BackgroundImage(new Image("/main/resources/tournois.png",665,395,false,false),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        topPart.setBackground(new Background(tournoiImage));

        for(int i=1;i<16;i++){
            listCheckJoueurs.add(new Label());
        }

        //Vbox check right wrong + results
        VBox vBoxCheckQuarter = new VBox();
        vBoxCheckQuarter.setMinWidth(35);
        vBoxCheckQuarter.setMaxWidth(35);


        //Vbox joueurs input quart de final
        VBox vBoxJoueurQuarter = new VBox();
        vBoxJoueurQuarter.setPadding(new Insets(0, 14, 0, 0));

        //Création des labels
        int[] inputCoords = {45,50,77,81,108,112,140,144,92,96,218,222,185,189,200};

        for(int i=0;i<inputCoords.length;i++){
            listJoueursInput.add(makeInputJoueur(inputCoords[i], listCheckJoueurs.get(i)));
            if(i>7){
                listJoueursInput.get(i).setDisable(true);
            }
        }

        //Vbox check semifinal
        VBox vBoxCheckSemi = new VBox();
        vBoxCheckSemi.setMinWidth(35);
        vBoxCheckSemi.setMaxWidth(35);
        vBoxCheckSemi.setPadding(new Insets(0, 0, 0, 0));
        listCheckJoueurs.set(8,makeLabelPrediction(1,89));
        listCheckJoueurs.set(9,makeLabelPrediction(1,87));
        listCheckJoueurs.set(10,makeLabelPrediction(1,200));
        listCheckJoueurs.set(11,makeLabelPrediction(1,197));

        //Vbox joueurs input semi final
        VBox vBoxJoueurSemi = new VBox();
        vBoxJoueurSemi.setPadding(new Insets(0, 14, 0, 0));

        //Vbox check final
        VBox vBoxCheckFinal = new VBox();
        vBoxCheckFinal.setPadding(new Insets(0, 0, 0, 0));
        listCheckJoueurs.set(12,makeLabelPrediction(3,181));
        listCheckJoueurs.set(13,makeLabelPrediction(3,179));


        //Vbox joueurs input final
        VBox vBoxJoueurFinal = new VBox();
        vBoxJoueurFinal.setPadding(new Insets(0, 14, 0, 0));

        //Vbox check final
        VBox vBoxCheckChampion = new VBox();
        vBoxCheckChampion.setPadding(new Insets(0, 0, 0, 0));
        listCheckJoueurs.set(14,makeLabelPrediction(3,198));

        //Vbox joueurs input final
        VBox vBoxJoueurChampion = new VBox();
        vBoxJoueurChampion.setPadding(new Insets(0, 0, 0, 0));

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
        vBoxCheckChampion.getChildren().addAll(listCheckJoueurs.get(14));
        vBoxJoueurChampion.getChildren().addAll(listJoueursInput.get(14));

        topPart.getChildren().addAll(vBoxCheckQuarter,vBoxJoueurQuarter,vBoxCheckSemi,vBoxJoueurSemi,vBoxCheckFinal,vBoxJoueurFinal,vBoxCheckChampion,vBoxJoueurChampion);

        HBox actionBar = new HBox();
        Button validateButton = new Button("Démarrer la prédiction");
        validateButton.getStyleClass().add("btn-primary");
        validateButton.setAlignment(Pos.CENTER);
        validateButton.setOnMouseClicked(e-> {
            if(!Arrays.asList(checkValide).contains(false)) {
                predictJoueursHomeMade();
            }
            else{
                InformationDialog informationDialog = new InformationDialog();
                informationDialog.makeDialog("Prédiction", "La prédiction n'a pas pu démarrer", "Vous ne pouvez pas exécuter une prédiction sans avoir validé tous les champs.");
            }
        });
        actionBar.setMinWidth(700);

        Button validate2Button = new Button("Réinitialiser");
        validate2Button.getStyleClass().add("btn-primary");
        validate2Button.setAlignment(Pos.CENTER);
        validate2Button.setOnMouseClicked(e-> createPredictionHBox(borderPane));
        actionBar.setAlignment(Pos.CENTER);
        actionBar.setSpacing(5);

        actionBar.getChildren().addAll(validateButton, validate2Button);

        content.getChildren().addAll(topPart,actionBar);

        center.getChildren().addAll(content);
        center.setAlignment(Pos.CENTER);
        borderPane.setCenter(center);

        //requete get joueurs
        JoueursDAO joueursDAO = new JoueursDAO();
        joueursDAO.getJoueurs(listJoueurs);

        for(int i=0;i<8;i++){
            inputAddAutoCompletion(listJoueursInput.get(i));
        }
    }

    /**
     * Création de l'input
     * @param y Position verticale
     * @param label Conteneur
     * @return Retourne l'input
     */
    private AutoCompletionTextField makeInputJoueur(int y, Label label){

        AutoCompletionTextField actualJoueur = new AutoCompletionTextField(label, true);
        actualJoueur.setMaxWidth(120);
        actualJoueur.setMinHeight(31);
        actualJoueur.getStyleClass().add("inputtransparent");
        actualJoueur.setTranslateY(y);
        return actualJoueur;
    }

    /**
     * Création du conteneur
     * @param x Position horizontale
     * @param y Position verticale
     * @return Retourne le conteneur
     */
    private Label makeLabelPrediction(int x, int y){
        Label label = new Label();
        label.setTranslateY(y);
        label.setTranslateX(x);
        label.setVisible(true);
        return label;
    }

    /**
     * Calcul des résultats
     */
    private void predictJoueursHomeMade() {
        for(int i=0;i<15;i++){
            listJoueursInput.get(i).getStyleClass().add("tf-disabled");
            listJoueursInput.get(i).setStyle("-fx-font-weight: bold");
        }

        MatchsDAO matchsDAO = new MatchsDAO();
        List<Match> list = matchsDAO.getMatchs();
        JoueursDAO joueursDAO = new JoueursDAO();
        int maxId = joueursDAO.getMaxId();

        //Initialisation du tableau d'elo (à 1000)
        points = new int[maxId+1][2];
        for (Match match : list) {
            if (points[match.getIdjoueur1()][0]!=match.getIdjoueur1()){
                points[match.getIdjoueur1()][0]=match.getIdjoueur1();
                points[match.getIdjoueur1()][1]=1000;
            }
            if (points[match.getIdjoueur2()][0]!=match.getIdjoueur2()){
                points[match.getIdjoueur2()][0]=match.getIdjoueur2();
                points[match.getIdjoueur2()][1]=1000;
            }
        }

        List<Integer> listId = Arrays.asList(Arrays.copyOfRange(idArray,1,idArray.length));

        Map<Integer,String> resultElo = new HashMap<>();
        for(int i=1;i<idArray.length;i++){
            resultElo.put(idArray[i],"1000");
        }

        //Pour chaque match on récupère les jeux, puis calcul le nouvel elo des joueurs du match
        for (Match match : list) {
            int[][] jeux = new int[5][2];
            String[] parsed = match.getJeux().split(",");
            int nbParsed=0;
            for(int i=0;i<5;i++){
                for(int y=0;y<2;y++){
                    if(parsed.length > nbParsed){
                        try {
                            jeux[i][y] = Integer.parseInt(parsed[nbParsed]);
                        } catch (NumberFormatException ex) {
                            jeux[i][y] = 0;
                            InformationDialog informationDialog = new InformationDialog();
                            informationDialog.makeDialog("Erreur informations joueurs", "Une erreur est survenue lors de la récupération des informations", "Une erreur est survenue lors de la récupération des informations du joueurs. Veuillez contacter votre administrateur.\n" + ex);
                        }
                    }else{
                        jeux[i][y] = 0;
                    }
                    nbParsed++;
                }
            }
            String set = match.getScore();
            int set1 = Integer.parseInt(String.valueOf(set.charAt(0)));
            int set2 = Integer.parseInt(String.valueOf(set.charAt(2)));

            int pointJoueur1 = (jeux[0][0] + jeux[1][0] + jeux[2][0] + jeux[3][0] + jeux[4][0]) / (set1 + set2);
            int pointJoueur2 = (jeux[0][1] + jeux[1][1] + jeux[2][1] + jeux[3][1] + jeux[4][1]) / (set1 + set2);

            double ecart;
            double j1;
            double j2;
            if (set1 > set2) {
                j1 = 0.15 * (points[match.getIdjoueur2()][1] - points[match.getIdjoueur1()][1]) + 75;
                if(j1<0){
                    j1=0;
                }
                j2 = j1 * -1;
                ecart = 0.186667 * (pointJoueur1 - pointJoueur2) + 0.248889;
            } else {
                j2 = 0.15 * (points[match.getIdjoueur1()][1] - points[match.getIdjoueur2()][1]) + 15;
                j1 = j2 * -1;
                ecart = 0.186667 * (pointJoueur2 - pointJoueur1) + 0.248889;
                if(j2<0){
                    j2=0;
                }
            }
            points[match.getIdjoueur1()][1] += ecart * j1;
            points[match.getIdjoueur2()][1] += ecart * j2;
            if (points[match.getIdjoueur2()][1] < 1) {
                points[match.getIdjoueur2()][1] = 1;
            }
            if (points[match.getIdjoueur1()][1] < 1) {
                points[match.getIdjoueur1()][1] = 1;
            }

            //On récupère la progression de chaque joueurs
            if(listId.contains(match.getIdjoueur1())){
                String stringElo = resultElo.get(match.getIdjoueur1());
                stringElo+=","+points[match.getIdjoueur1()][1];
                resultElo.put(match.getIdjoueur1(),stringElo);
            }
            if (listId.contains(match.getIdjoueur2())){
                String stringElo = resultElo.get(match.getIdjoueur2());
                stringElo+=","+points[match.getIdjoueur2()][1];
                resultElo.put(match.getIdjoueur2(),stringElo);
            }
        }

        //Recuperation des id, nom et prenom des 8 joueurs du tournoi
        Map<Integer,String> mapNomJoueurs = new HashMap<>();
        String sqlId="";
        for(int i=1;i<9;i++){
            if(i>1){
                sqlId+=" OR id="+idArray[i];
            }
            mapNomJoueurs.put(idArray[i], "");
        }

        joueursDAO.fillMapNomJoueurs(idArray[1], sqlId, mapNomJoueurs);

        Double[] probaVainqueur = new Double[7];
        int joueursDiff;
        int[] vainqueursPrediction = new int[7];
        int compteurMatchs=0;

        //Boucle sur les inputs renseigné pour calculer l'elo et afficher leurs proba
        for(int i=1;i<8;i+=2){
            joueursDiff = points[idArray[i]][1] - points[idArray[i+1]][1];
            if(joueursDiff<0){
                joueursDiff*=-1;
            }
            double probaVictoire;
            double probaDefaite;
            if(joueursDiff<35){
                probaVictoire = 0.2*joueursDiff+50;
                probaDefaite = 100-(0.2*joueursDiff+50);
            }
            else if(joueursDiff>=35 & joueursDiff<=500){
                probaVictoire = 0.0715406*joueursDiff+55;
                probaDefaite = 100-(0.0715406*joueursDiff+55);
            }
            else{
                probaVictoire = 0.02*joueursDiff+80;
                probaDefaite = 100-(0.02*joueursDiff+80);
                if(probaVictoire>99){
                    probaVictoire=99.0;
                    probaDefaite=1.0;
                }
            }

            //set les proba des 8 premier inputs
            if(points[idArray[i+1]][1] > points[idArray[i]][1]){
                vainqueursPrediction[compteurMatchs]=idArray[i+1];
                listCheckJoueurs.get(i).setText(String.valueOf(probaVictoire).split("\\.")[0]);
                listCheckJoueurs.get(i).setTextFill(Color.GREEN);

                listCheckJoueurs.get(i-1).setText(String.valueOf(probaDefaite).split("\\.")[0]);
                listCheckJoueurs.get(i-1).setTextFill(Color.RED);
            }else{
                vainqueursPrediction[compteurMatchs]=idArray[i];
                listCheckJoueurs.get(i).setText(String.valueOf(probaDefaite).split("\\.")[0]);
                listCheckJoueurs.get(i).setTextFill(Color.RED);

                listCheckJoueurs.get(i-1).setText(String.valueOf(probaVictoire).split("\\.")[0]);
                listCheckJoueurs.get(i-1).setTextFill(Color.GREEN);
            }
            probaVainqueur[compteurMatchs] = probaVictoire;
            listCheckJoueurs.get(i).setGraphic(null);
            listCheckJoueurs.get(i).setStyle("-fx-font-weight: bold");

            listCheckJoueurs.get(i-1).setGraphic(null);
            listCheckJoueurs.get(i-1).setStyle("-fx-font-weight: bold");
            compteurMatchs++;
        }

        //Boucle calcul des proba selon elo
        for(int i=0;i<5;i+=2){
            joueursDiff = points[vainqueursPrediction[i]][1] - points[vainqueursPrediction[i+1]][1];
            if(joueursDiff<0){
                joueursDiff*=-1;
            }
            double probaVictoire;
            double probaDefaite;
            if(joueursDiff<35){
                probaVictoire = 0.2*joueursDiff+50;
                probaDefaite = 100-(0.2*joueursDiff+50);
            }
            else if(joueursDiff>=35 & joueursDiff<=500){
                probaVictoire = 0.0715406*joueursDiff+55;
                probaDefaite = 100-(0.0715406*joueursDiff+55);
            }
            else{
                probaVictoire = 0.02*joueursDiff+80;
                probaDefaite = 100-(0.02*joueursDiff+80);
                if(probaVictoire>99){
                    probaVictoire=99.0;
                    probaDefaite=1.0;
                }
            }

            //set les labels de resultats
            if(points[vainqueursPrediction[i+1]][1] > points[vainqueursPrediction[i]][1]){
                vainqueursPrediction[compteurMatchs]=vainqueursPrediction[i+1];
                listCheckJoueurs.get(9+i).setText(String.valueOf(probaVictoire).split("\\.")[0]);
                listCheckJoueurs.get(9+i).setTextFill(Color.GREEN);

                listCheckJoueurs.get(8+i).setText(String.valueOf(probaDefaite).split("\\.")[0]);
                listCheckJoueurs.get(8+i).setTextFill(Color.RED);

                probaVainqueur[compteurMatchs] = (probaVictoire * probaVainqueur[i+1]) / 100.0;
                if(i==4){
                    listJoueursInput.get(14).setText(mapNomJoueurs.get(vainqueursPrediction[i+1]));
                    probaVainqueur[compteurMatchs] = (probaVictoire * probaVainqueur[5]) / 100.0;
                }

            }else{
                vainqueursPrediction[compteurMatchs]=vainqueursPrediction[i];
                listCheckJoueurs.get(9+i).setText(String.valueOf(probaDefaite).split("\\.")[0]);
                listCheckJoueurs.get(9+i).setTextFill(Color.RED);

                listCheckJoueurs.get(8+i).setText(String.valueOf(probaVictoire).split("\\.")[0]);
                listCheckJoueurs.get(8+i).setTextFill(Color.GREEN);

                probaVainqueur[compteurMatchs] = (probaVictoire * probaVainqueur[i]) / 100.0;
                if(i==4){
                    listJoueursInput.get(14).setText(mapNomJoueurs.get(vainqueursPrediction[i]));
                    probaVainqueur[compteurMatchs] = (probaVictoire * probaVainqueur[4]) / 100.0;
                }
            }
            listJoueursInput.get(8+i).setText(mapNomJoueurs.get(vainqueursPrediction[i]));
            listCheckJoueurs.get(8+i).setStyle("-fx-font-weight: bold");

            listJoueursInput.get(9+i).setText(mapNomJoueurs.get(vainqueursPrediction[i+1]));
            listCheckJoueurs.get(9+i).setStyle("-fx-font-weight: bold");
            compteurMatchs++;
        }
        listCheckJoueurs.get(14).setText(String.valueOf(probaVainqueur[6]).split("\\.")[0]);
        listCheckJoueurs.get(14).setTextFill(Color.GREEN);
        listCheckJoueurs.get(14).setStyle("-fx-font-weight: bold");
        listJoueursInput.get(14).setStyle("-fx-font-weight: bold");

        for(int i=0;i<7;i+=2){
            showGraph(listJoueursInput.get(i),listJoueursInput.get(i+1),resultElo.get(idArray[i+1]),resultElo.get(idArray[i+2]));
            if(i<5) {
                listJoueursInput.get(8+i).setDisable(false);

                listJoueursInput.get(9+i).setDisable(false);
                showGraph(listJoueursInput.get(8 + i), listJoueursInput.get(9 + i), resultElo.get(vainqueursPrediction[i]), resultElo.get(vainqueursPrediction[i + 1]));
            }
        }
    }


    /**
     * Ajouts des listeners liés aux graphiques
     * @param joueur1 Input du premier joueur
     * @param joueur2 Input du second joueur
     * @param result1 Progression de l'élo du premier joueur
     * @param result2 Progression de l'élo du second joueur
     */
    private void showGraph(AutoCompletionTextField joueur1, AutoCompletionTextField joueur2, String result1, String result2) {
        joueur1.focusedProperty().addListener((observable, oldValue, newValue) -> joueur1.getParent().requestFocus());
        joueur2.focusedProperty().addListener((observable, oldValue, newValue) -> joueur2.getParent().requestFocus());
        joueur1.setOnMousePressed(e -> {
            createGraph(result1,result2, joueur1.getText(), joueur2.getText());
            joueur1.getParent().requestFocus();
        });
        joueur2.setOnMousePressed(e -> {
            createGraph(result1,result2, joueur1.getText(), joueur2.getText());
            joueur1.getParent().requestFocus();
        });
    }

    /**
     * Ajout de la liste des joueurs dans les inputs
     * @param textField Input de sélection d'un joueur
     */
    private void inputAddAutoCompletion(AutoCompletionTextField textField){
        textField.getText();
        textField.getEntries().addAll(listJoueurs);
    }

    /**
     * Création du graphique
     * @param result1 Progression de l'élo du premier joueur
     * @param result2 Progression de l'élo du second joueur
     * @param joueur1Nom Nom du premier joueur
     * @param joueur2Nom Nom du second joueur
     */
    private void createGraph(String result1,String result2, String joueur1Nom, String joueur2Nom){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().setStyle("-fx-border-color: #000000; -fx-border-width: 0.5;");

        // Set the button types.
        ButtonType validateButtonType = new ButtonType("Fermer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validateButtonType);

        String[] joueur1Data = result1.split(",");
        String[] joueur2Data = result2.split(",");

        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Nombre de match");
        xAxis.setAutoRanging(false);
        int max = Math.max(joueur1Data.length, joueur2Data.length);
        int unit=1;
        if(max>10){
            unit=max/10;
        }
        xAxis.setTickUnit(unit);
        xAxis.setAutoRanging(false);
        xAxis.setMinorTickVisible(false);
        xAxis.setUpperBound(max);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre de points");



        //creating the chart
        final LineChart<Number,Number> lineChart =  new LineChart(xAxis,yAxis);

        lineChart.setTitle("Evolution des deux joueurs");
        //defining a series
        XYChart.Series seriesJ1 = new XYChart.Series();
        XYChart.Series seriesJ2 = new XYChart.Series();

        seriesJ1.setName(joueur1Nom);
        seriesJ2.setName(joueur2Nom);


        //populating the series with data
        for(int i=0;i<joueur1Data.length;i++){
            seriesJ1.getData().add(new XYChart.Data(i, Integer.parseInt(joueur1Data[i])));
        }
        for(int i=0;i<joueur2Data.length;i++){
            seriesJ2.getData().add(new XYChart.Data(i, Integer.parseInt(joueur2Data[i])));
        }

        lineChart.getData().addAll(seriesJ1,seriesJ2);
        lineChart.setCreateSymbols(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.setPadding(new Insets(25, 40, 10, 40));

        grid.add(lineChart, 0, 0);
        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    }
}