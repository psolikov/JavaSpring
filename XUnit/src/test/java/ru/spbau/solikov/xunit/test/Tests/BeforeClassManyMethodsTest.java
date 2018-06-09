package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.BeforeClass;
import ru.spbau.solikov.xunit.annotations.Test;

public class BeforeClassManyMethodsTest {

    public static int beforeClass = 0;

    @BeforeClass
    public void setBeforeClass(){
        System.out.println("setBeforeClass");
        beforeClass++;
    }

    @Test
    public void test(){
    }

    @Test
    public void test2(){
    }

    @Test
    public void test3(){
    }
}
