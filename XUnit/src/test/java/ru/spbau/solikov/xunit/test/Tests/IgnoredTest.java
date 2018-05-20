package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.Test;

public class IgnoredTest {

    @Test(ignore = "123")
    public void test(){
    }
}
