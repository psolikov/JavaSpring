package ru.spbau.solikov.xunit.test.Tests;

import ru.spbau.solikov.xunit.annotations.Test;

import java.security.GeneralSecurityException;

public class WrongException {

    @Test(expected = LayerInstantiationException.class)
    public void test() throws GeneralSecurityException {
        throw new GeneralSecurityException();
    }
}
