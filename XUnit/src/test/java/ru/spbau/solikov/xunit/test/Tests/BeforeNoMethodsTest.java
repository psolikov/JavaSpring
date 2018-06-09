package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.Before;

public class BeforeNoMethodsTest {
    public static boolean beforeCalled = false;

    @Before
    public void before() {
        beforeCalled = true;
    }
}
