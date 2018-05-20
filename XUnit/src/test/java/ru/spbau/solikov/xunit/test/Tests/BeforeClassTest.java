package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.BeforeClass;
import ru.spbau.solikov.xunit.annotations.Test;

public class BeforeClassTest {

    public static boolean beforeClass = false;

    @BeforeClass
    public void setBeforeClass(){
        beforeClass = true;
    }

    @Test
    public void test(){
    }
}
