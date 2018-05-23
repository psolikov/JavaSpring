package ru.spbau.solikov.pairs.test;

import org.junit.Test;
import ru.spbau.solikov.pairs.Logic;
import ru.spbau.solikov.pairs.Pairs;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the logic of the game.
 */
public class LogicTest {

    @Test
    public void testSelectedButtonClickedDoesNotEffect() {
        Logic logic = new Logic();
        assertEquals(1, logic.buttonSelected(0, 0).size());
        assertEquals(0, logic.buttonSelected(0, 0).size());
    }

    @Test
    public void testSelectedButtonReturnsIntValue() {
        Logic logic = new Logic();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < Pairs.getN() * Pairs.getN(); i++){
            list.add(String.valueOf(i));
        }
        String s = logic.buttonSelected(0, 0).get(0).getMessage();
        assertTrue(list.contains(s));
    }
}
