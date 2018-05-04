package ru.spbau.solikov.pairs;

import java.util.*;

/**
 * Logic part of the game. Stores 2D field of buttons and their states.
 * Used in class "Pairs".
 */
public class Logic {

    private int[][] game;
    private int[][] isOpened;
    private int left;

    /**
     * Represents a card that was (or not) selected on the previous click.
     */
    private class Card {
        int getA() {
            return a;
        }

        private int a;

        int getB() {
            return b;
        }

        private int b;

        Card(int a, int b) {
            this.a = a;
            this.b = b;
        }

        int getGameNumber() {
            return game[a][b];
        }

        void close() {
            isOpened[a][b] = -1;
        }

        void delete() {
            isOpened[a][b] = 0;
        }
    }

    private Card selected = null;

    /**
     * Starts the game logic by filling the board with random pairs in range 0..N/2.
     * Each pair is unique.
     * Uses shuffle method.
     */
    public Logic() {
        game = new int[Pairs.getN()][Pairs.getN()];
        isOpened = new int[Pairs.getN()][Pairs.getN()];
        for (int[] array : isOpened) {
            Arrays.fill(array, -1);
        }

        left = Pairs.getN() * Pairs.getN();
        generateBoard();
    }

    private void generateBoard(){
        List<Integer> list = new ArrayList<>(Pairs.getN() * Pairs.getN());
        for (int i = 0; i < Pairs.getN() * Pairs.getN() / 2; i++){
            list.add(i);
            list.add(i);
        }

        Collections.shuffle(list);

        for (int i = 0; i < Pairs.getN(); i++){
            for (int j = 0; j < Pairs.getN(); j++){
                game[i][j] = list.get(Pairs.getN() * i + j);
            }
        }
    }

    /**
     * Is invoked when button was selected. Processes current button and previous button.
     * After that gives some work to main class. Marks cards that are not in game with "*".
     * Does not affect selected button.
     *
     * @param a first coordinate of the button
     * @param b second coordinate of the button
     * @return list of all changes needed to be done with buttons. I.e. turn the card upside-down.
     */
    public List<ChangeButton> buttonSelected(int a, int b) {
        List<ChangeButton> todo = new ArrayList<>();

        if (isOpened[a][b] != -1) {
            return todo;
        }

        if (selected != null) {
            if (game[a][b] == selected.getGameNumber()) {
                todo.add(new ChangeButton(a, b, "*"));
                todo.add(new ChangeButton(selected.getA(), selected.getB(), "*"));
                isOpened[a][b] = 0;
                selected.delete();
                selected = null;
                left -= 2;
            } else {
                todo.add(new ChangeButton(a, b, ""));
                todo.add(new ChangeButton(selected.getA(), selected.getB(), ""));
                isOpened[a][b] = -1;
                selected.close();
                selected = null;
            }
        } else {
            todo.add(new ChangeButton(a, b, String.valueOf(game[a][b])));
            selected = new Card(a, b);
            isOpened[a][b] = 1;
        }

        if (left == 0) {
            todo.add(new ChangeButton());
        }

        return todo;
    }

    /**
     * Class that represents changes needed to be done after clicking the button.
     */
    public static class ChangeButton {
        public String getMessage() {
            return message;
        }

        int getI() {
            return i;
        }

        int getJ() {
            return j;
        }

        private String message;
        private int i;
        private int j;

        /**
         * Checks if game finished and need to show win message.
         *
         * @return is finished
         */
        boolean isWin() {
            return win;
        }

        private boolean win = false;

        /**
         * Constructor for simple button changes.
         *
         * @param i first coordinate
         * @param j second coordinate
         * @param message to be shown
         */
        ChangeButton(int i, int j, String message) {
            this.i = i;
            this.j = j;
            this.message = message;
        }

        /**
         * Constructor for win event message.
         */
        ChangeButton(){
            win = true;
        }
    }
}
