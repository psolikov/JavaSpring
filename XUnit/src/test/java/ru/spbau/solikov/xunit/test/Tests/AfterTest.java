package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.After;
import ru.spbau.solikov.xunit.annotations.Test;

public class AfterTest {

    public static boolean afterCalled = false;

    @After
    public void before() {
        afterCalled = true;
    }

    @Test
    public void test() {
    }
}
