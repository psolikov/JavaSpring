package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.Before;
import ru.spbau.solikov.xunit.annotations.Test;

public class BeforeManyMethodsTest {

    public static int beforeCalled = 0;

    @Before
    public void before(){
        beforeCalled++;
    }

    @Test
    public void test1(){
    }

    @Test
    public void test2(){
    }

    @Test
    public void test3(){
    }
}
