package ru.spbau.solikov.xunit;

import ru.spbau.solikov.xunit.annotations.*;

public class Main {
    public static void main(String[] args) {
        var kek = 2;
        System.out.println(kek);
    }

    @BeforeClass
    public void bc(){System.out.println("bc");}

    @AfterClass
    public void ac(){System.out.println("ac");}


    @Before
    public void b(){
System.out.println("b");
    }

    @After
    public void a(){
        System.out.println("a");
    }

    @Test
    public void kek(){
        System.out.println("sdsds" );
    }

    @Test
    public void kek2(){
        System.out.println("sdsds2" );
    }
}
