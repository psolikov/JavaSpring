package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.BeforeClass;

public class BeforeClassNoMethods {

    public static int beforeClass = 0;

    @BeforeClass
    public void setBeforeClass(){
        beforeClass++;
    }
}
