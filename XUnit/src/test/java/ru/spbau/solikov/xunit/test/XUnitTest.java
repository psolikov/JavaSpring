package ru.spbau.solikov.xunit.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.solikov.xunit.XUnit;
import ru.spbau.solikov.xunit.test.Tests.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class XUnitTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void testTestAnnotationInvokesMethod() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(TestAnnotationTest.class);
        assertTrue(TestAnnotationTest.tested);
    }

    @Test
    public void testBeforeOneMethod() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(BeforeTest.class);
        assertTrue(BeforeTest.beforeCalled);
    }

    @Test
    public void testBeforeNoMethods() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(BeforeNoMethodsTest.class);
        assertFalse(BeforeNoMethodsTest.beforeCalled);
    }

    @Test
    public void testBeforeManyMethods() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(BeforeManyMethodsTest.class);
        assertEquals(3, BeforeManyMethodsTest.beforeCalled);
    }

    @Test
    public void testAfterOneMethod() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(AfterTest.class);
        assertTrue(AfterTest.afterCalled);
    }

    @Test
    public void testAfterNoMethods() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(AfterNoMethods.class);
        assertFalse(AfterNoMethods.afterCalled);
    }

    @Test
    public void testAfterManyMethods() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(AfterManyMethodsTest.class);
        assertEquals(3, AfterManyMethodsTest.afterCalled);
    }

    @Test
    public void testBeforeClass() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(BeforeClassTest.class);
        assertTrue(BeforeClassTest.beforeClass);
    }

    @Test
    public void testBeforeClassManyMethods() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(BeforeClassManyMethodsTest.class);
        assertEquals(1, BeforeClassManyMethodsTest.beforeClass);
    }

    @Test
    public void testBeforeClassNoMethods() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(BeforeClassNoMethods.class);
        assertEquals(1, BeforeClassNoMethods.beforeClass);
    }

    @Test
    public void testException() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(ExceptionTest.class);
        assertTrue(outContent.toString().contains("finished"));
    }

    @Test
    public void testExpectedExceptionButNotFound() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(ExpectedException.class);
        assertTrue(outContent.toString().contains("failed"));
    }

    @Test
    public void testWrongExpectedException() throws
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        var xUnit = new XUnit<>(WrongException.class);
        assertTrue(outContent.toString().contains("failed"));
    }
}
