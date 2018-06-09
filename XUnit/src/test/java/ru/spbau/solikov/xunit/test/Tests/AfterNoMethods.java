package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.After;

public class AfterNoMethods {

    public static boolean afterCalled = false;

    @After
    public void before() {
        afterCalled = true;
    }
}
