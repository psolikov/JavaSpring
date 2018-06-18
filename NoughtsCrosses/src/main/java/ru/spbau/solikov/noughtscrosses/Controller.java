package ru.spbau.solikov.noughtscrosses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logic of the game.
 */
public class Controller {

    boolean isOver = false;
    private Player currentPlayer = Player.Crosses;
    private Player win = null;
    private int count = 0;
    private int totalDraw = 0;
    private int totalNoughts = 0;
    private int totalCrosses = 0;

    /**
     * Starts the game and creates a controller.
     */
    public Controller() {
        newGame();
    }

    /**
     * Starts the new game.
     */
    public void newGame() {
        currentPlayer = Player.Crosses;
        win = null;
        count = 0;
        isOver = false;
    }

    private Player getNextPlayer() {
        return currentPlayer == Player.Crosses ? Player.Noughts : Player.Crosses;
    }

    private String getPlayersSymbol(Player player) {
        if (player == Player.Crosses) {
            return "X";
        }

        if (player == Player.Noughts) {
            return "O";
        }

        if (player == Player.Friendship) {
            return "Draw";
        }

        return "";
    }

    /**
     * Handles the turns from user and bot.
     *
     * @param i     row of button
     * @param j     column of button
     * @param isBot if the method was called from bot
     */
    public void takeTurn(int i, int j, boolean isBot) {
        count++;

        currentPlayer = getNextPlayer();
        if (Main.board[i][j].getText().equals("")) {
            Main.board[i][j].setText(getPlayersSymbol(currentPlayer));
            Main.board[i][j].setDisable(true);
        }

        if (count == 9) {
            win = Player.Friendship;
        }

        searchWinner();

        if (win != null) {
            for (int n = 0; n < 3; n++) {
                for (int m = 0; m < 3; m++) {
                    Main.board[n][m].setDisable(true);
                }
            }

            isOver = true;
            searchWinner();

            if (win == Player.Friendship) {
                totalDraw++;
            } else if (win == Player.Noughts) {
                totalNoughts++;
            } else {
                totalCrosses++;
            }

            Main.listView.getItems().set(0, "Total: Crosses - " + totalCrosses + ",Noughts - "
                    + totalNoughts + ", DRAW - " + totalDraw);
            Main.listView.getItems().add(getPlayersSymbol(win) +
                    " : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                    "uuuu-MMM-dd  hh:mm")));
            Main.showWinner(getPlayersSymbol(win));
        }

        if (Main.withBot && !isBot) {
            Main.bot.takeTurn(this);
        }
    }

    private void searchWinner() {
        for (int i = 0; i < 3; i++) {
            searchWinnerInColumn(i, Player.Noughts);
            searchWinnerInColumn(i, Player.Crosses);
            searchWinnerInRow(i, Player.Noughts);
            searchWinnerInRow(i, Player.Crosses);
        }

        searchWinnerInDiagonal(Player.Noughts);
        searchWinnerInDiagonal2(Player.Noughts);
        searchWinnerInDiagonal(Player.Crosses);
        searchWinnerInDiagonal2(Player.Crosses);
    }

    private void searchWinnerInRow(int i, Player player) {
        for (int j = 0; j < 3; j++) {
            if (!Main.board[i][j].getText().equals(getPlayersSymbol(player))) {
                return;
            }
        }

        win = player;
    }

    private void searchWinnerInColumn(int j, Player player) {
        for (int i = 0; i < 3; i++) {
            if (!Main.board[i][j].getText().equals(getPlayersSymbol(player))) {
                return;
            }
        }

        win = player;
    }

    private void searchWinnerInDiagonal(Player player) {
        for (int j = 0; j < 3; j++) {
            if (!Main.board[j][j].getText().equals(getPlayersSymbol(player))) {
                return;
            }
        }

        win = player;
    }

    private void searchWinnerInDiagonal2(Player player) {
        for (int j = 0; j < 3; j++) {
            if (!Main.board[j][3 - j - 1].getText().equals(getPlayersSymbol(player))) {
                return;
            }
        }

        win = player;
    }

    /**
     * Represents the player's side.
     */
    private enum Player {
        Crosses, Noughts, Friendship
    }
}