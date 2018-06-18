package ru.spbau.solikov.noughtscrosses;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class that is responsible for UI and application.
 */
public class Main extends Application {

    private static Stage primaryStage;
    private static Scene menu;
    private static Scene game;
    private static Scene statistics;
    private static Controller controller = new Controller();

    static Button[][] board = new Button[3][3];
    static ListView<String> listView = new ListView<>();
    static Bot bot = new Bot();
    static boolean withBot = false;

    /**
     * Steps that will be done wile starting the application.
     *
     * @param primaryStage given stage
     */
    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        setUpMenu();
        setUpBoard();
        setUpStatistics();
        primaryStage.setTitle("Noughts&Crosses");
        primaryStage.setScene(menu);
        primaryStage.show();
    }

    private static void setUpMenu() {
        Button singlePlayer = new Button("Single-Player mode");
        singlePlayer.setPrefSize(600, 300);
        Button multiPlayer = new Button("Multi-Player mode");
        multiPlayer.setPrefSize(600, 300);

        VBox vBox = new VBox(singlePlayer, multiPlayer);
        singlePlayer.setOnAction(event -> {
            primaryStage.setScene(game);
            withBot = true;
        });
        multiPlayer.setOnAction(event -> {
            primaryStage.setScene(game);
            withBot = false;
        });

        menu = new Scene(vBox, 600, 600);
    }

    private static void setUpBoard() {
        GridPane gridPane = new GridPane();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int i1 = i, j1 = j;
                board[i][j] = new Button();
                board[i][j].setPrefSize(200, 200);
                board[i][j].setOnAction(event -> controller.takeTurn(i1, j1, false));
                gridPane.add(board[i][j], i, j);
            }
        }

        Button menuButton = new Button("Menu");
        menuButton.setPrefSize(200, 80);
        menuButton.setOnAction(event -> primaryStage.setScene(menu));
        Button newGameButton = new Button("New Game");
        newGameButton.setPrefSize(200, 80);
        newGameButton.setOnAction(event -> newGame());
        Button statisticsButton = new Button("Statistics");
        statisticsButton.setPrefSize(200, 80);
        statisticsButton.setOnAction(event -> primaryStage.setScene(statistics));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.getChildren().add(menuButton);
        hBox.getChildren().add(newGameButton);
        hBox.getChildren().add(statisticsButton);
        VBox vBox = new VBox(10, gridPane, hBox);

        game = new Scene(vBox, 600, 700);
    }

    private static void setUpStatistics() {
        listView.getItems().add("Total: Crosses - " + 0 + ",Noughts - "
                + 0 + ", DRAW - " + 0);

        Button button = new Button("Back");
        button.setPrefSize(300, 20);
        button.setOnAction(event -> primaryStage.setScene(game));

        VBox vbox = new VBox(listView, button);

        statistics = new Scene(vbox, 300, 300);
        primaryStage.setScene(statistics);
    }

    private static void newGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setText("");
                board[i][j].setDisable(false);
            }
        }
        controller.newGame();
    }

    static void showWinner(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Winner : " + winner);

        alert.showAndWait();
    }

    /**
     * Main function to make program more readable.
     *
     * @param args arguments passed through command line
     */
    public static void main(String[] args) {
        launch(args);
    }
}
