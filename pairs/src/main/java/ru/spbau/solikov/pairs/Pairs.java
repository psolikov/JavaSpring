package ru.spbau.solikov.pairs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A main part of the game. Responsible for graphics part. To start the game use main method.
 * You are given N*N field with empty cards. Pick 2 of them to check if they are similar.
 * If they are, they will be marked. Game ends when all cards are marked.
 */
public class Pairs extends Application {

    public static int getN() {
        return N;
    }

    private static int N = 4;
    private GridPane board = new GridPane();
    private Button[][] buttons;
    private Logic logic = new Logic();
    private Stage primaryStage;
    private final static int TIME_TO_WAIT = 1000;

    /**
     * Starts the application in own window with board, containing N*N buttons.
     *
     * @param primaryStage given primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Scene scene = new Scene(board);
        buttons = new Button[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                final int toI = i;
                final int toJ = j;
                buttons[i][j] = new Button();
                buttons[i][j].setOnAction(x -> logic.buttonSelected(toI, toJ)
                        .forEach(d -> {
                            if (d.isWin()) showWin();
                            else setText(d.getI(), d.getJ(), d.getMessage());
                        }));
                board.add(buttons[i][j], i, j);
            }
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showWin() {
        primaryStage.setScene(new Scene(new TextArea("WIN")));
    }

    private void setText(int i, int j, String s) {
        if (s.equals("")) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(TIME_TO_WAIT),
                    m -> buttons[i][j].setText("")
            ));
            timeline.play();
            return;
        }
        buttons[i][j].setText(s);
    }

    /**
     * Main function to make program more readable.
     *
     * @param args arguments passed through command line (size of the field)
     */
    public static void main(String[] args) {
        N = Integer.parseInt(args[0]);
        launch(args);
    }
}
