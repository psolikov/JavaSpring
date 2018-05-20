package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.Test;

public class TestAnnotationTest {

    public static boolean tested = false;

    @Test
    public void test(){
        tested = true;
    }

}
