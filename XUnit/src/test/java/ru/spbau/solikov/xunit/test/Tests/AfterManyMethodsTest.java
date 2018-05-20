package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.Before;
import ru.spbau.solikov.xunit.annotations.Test;

public class AfterManyMethodsTest {

    public static int afterCalled = 0;

    @Before
    public void before() {
        afterCalled++;
    }

    @Test
    public void test1() {
    }

    @Test
    public void test2() {
    }

    @Test
    public void test3() {
    }
}
