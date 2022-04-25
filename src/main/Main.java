package main;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.connexion.Connexion;
import main.rootpanels.MainWindow;
import main.rootpanels.windows.SetDraggableWindow;
import main.variables.Variables;


/**
 * Classe principale
 */
public class Main extends Application {
    public static final String SPLASH_IMAGE =
            "/main/resources/logo2.png";

    private VBox splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private static Stage primaryStage;
    private static final int SPLASH_WIDTH = 677;
    private static final int SPLASH_HEIGHT = 437;

    /**
     * Lancement de init() puis start()
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Création de l'écran de chargement
     */
    @Override
    public void init() {
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 100);
        progressText = new Label();
        progressText.setFont(Font.font(Variables.policeBasique, FontWeight.NORMAL, Variables.taillePoliceBasique));
        progressText.setTextFill(Color.web("#FFFFFF"));
        progressText.setWrapText(true);
        progressText.setTextAlignment(TextAlignment.CENTER);
        progressText.setPadding(new Insets(10,0,10,0));
        splashLayout = new VBox();
        splashLayout.setAlignment(Pos.BOTTOM_CENTER);
        splashLayout.setPrefHeight(SPLASH_HEIGHT);
        splashLayout.setPrefWidth(SPLASH_WIDTH);
        splashLayout.setStyle(" -fx-background-image: url("+SPLASH_IMAGE+") ");
        splashLayout.getChildren().addAll(loadProgress, progressText);
        splashLayout.setEffect(new DropShadow());
    }

    /**
     * Création des threads et lancement de l'application
     * @param initStage
     */
    @Override
    public void start(final Stage initStage) {
        initStage.getIcons().add(new Image("/main/resources/icon.png"));
        final Task<ObservableList<String>> friendTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> foundFriends =
                        FXCollections.<String>observableArrayList();
                ObservableList<String> availableFriends =
                        FXCollections.observableArrayList(
                                "Initialisation de la connexion", "Création de la structure", "Ajout des éléments", "Ajout des options secondaires", "Préparation de la fenêtre", "Lancement de l'application"
                        );
                for (int i = 0; i < availableFriends.size(); i++) {
                    Thread.sleep(200);
                    updateProgress(i + 1, availableFriends.size());
                    String nextFriend = availableFriends.get(i);
                    foundFriends.add(nextFriend);
                    updateMessage(nextFriend);
                }
                Thread.sleep(200);

                return foundFriends;
            }
        };

        showSplash(
                initStage,
                friendTask,
                () -> showMainStage()
        );
        new Thread(friendTask).start();
    }

    /**
     * Création et affichage du Stage Principal
     */
    private void showMainStage() {
        primaryStage = new Stage();
        Connexion.loadDriver();
        Connexion.createConnection();

        //Create main window
        MainWindow mainWindow = new MainWindow();
        Scene scene = new Scene(mainWindow.createMainBP(), 1000, 660);
        //Adding styles
        String styleSheet = getClass().getResource("resources/style.css").toExternalForm();
        scene.getStylesheets().add(styleSheet);
        //Remove border window
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        //Set Draggable
        SetDraggableWindow setDraggableWindow = new SetDraggableWindow();
        setDraggableWindow.setDraggable(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/main/resources/icon.png"));
        primaryStage.toFront();
        primaryStage.show();

        //Variables largeur/hauteur de center
        Variables.hauteurCenter= scene.getWindow().getHeight() - Variables.hauteurTopHBox;
        Variables.largeurCenter= scene.getWindow().getWidth() - Variables.largeurLeftVBox;
    }

    /**
     * Mise à jour de l'écran de chargement
     * @param initStage
     * @param task
     * @param initCompletionHandler
     */
    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    /**
     * Initialisation de l'écran de chargement.
     */
    public interface InitCompletionHandler {
        void complete();
    }
}
