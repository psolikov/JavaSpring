package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.Before;
import ru.spbau.solikov.xunit.annotations.Test;

public class BeforeTest {

    public static boolean beforeCalled = false;

    @Before
    public void before() {
        beforeCalled = true;
    }

    @Test
    public void test() {
    }
}
