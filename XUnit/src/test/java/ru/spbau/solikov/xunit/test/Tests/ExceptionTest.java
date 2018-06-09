package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.Test;

public class ExceptionTest {

    @Test(expected = RuntimeException.class)
    public void test(){
        throw new RuntimeException();
    }
}
