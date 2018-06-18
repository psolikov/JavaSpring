package ru.spbau.solikov.noughtscrosses;

import java.util.Random;

/**
 * Simple bot for single-player.
 */
public class Bot {

    /**
     * Picks random button and if possible takes turn.
     *
     * @param controller game process
     */
    public void takeTurn(Controller controller) {
        if (controller.isOver) {
            return;
        }

        Random rand = new Random();
        int i, j;
        do {
            i = (rand.nextInt() & Integer.MAX_VALUE) % 3;
            j = (rand.nextInt() & Integer.MAX_VALUE) % 3;
        } while (!Main.board[i][j].getText().equals(""));
        controller.takeTurn(i, j, true);
    }
}
