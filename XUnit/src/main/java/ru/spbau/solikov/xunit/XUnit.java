package ru.spbau.solikov.xunit;

import org.jetbrains.annotations.NotNull;
import ru.spbau.solikov.xunit.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class XUnit<T> {

    private T instance;

    private ArrayList<Method> tests = new ArrayList<>();
    private ArrayList<Method> before = new ArrayList<>();
    private ArrayList<Method> beforeClass = new ArrayList<>();
    private ArrayList<Method> after = new ArrayList<>();
    private ArrayList<Method> afterClass = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please, pass only classname");
            return;
        }

        var className = args[0];

        try {
            var aClass = Class.forName(className);
            new XUnit<>(aClass);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't find class " + className + " " + e.getMessage());
        } catch (NoSuchMethodException e) {
            System.out.println("Provided class doesn't have default constructor, " +
                    "can't make an instance " + e.getMessage());
        } catch (InstantiationException e) {
            System.out.println("Can't make an instance " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("Can't access method " + e.getMessage());
        } catch (InvocationTargetException e) {
            System.out.println("Can't invoke method " + e.getMessage());
        }
    }

    public XUnit(@NotNull Class<T> aClass) throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        instance = aClass.getDeclaredConstructor().newInstance();
        for (var method : aClass.getDeclaredMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                tests.add(method);
            }
            if (method.getAnnotation(After.class) != null) {
                after.add(method);
            }
            if (method.getAnnotation(Before.class) != null) {
                before.add(method);
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                afterClass.add(method);
            }
            if (method.getAnnotation(BeforeClass.class) != null) {
                beforeClass.add(method);
            }
        }
        runTest();
    }

    private void runTest() throws InvocationTargetException, IllegalAccessException {
        for (var beforeClassMethod : beforeClass) {
            beforeClassMethod.invoke(instance);
        }

        for (var test : tests) {
            var annotation = test.getAnnotation(Test.class);
            if (!annotation.ignore().equals("")) {
                System.out.println(test.getName() + " - ignored: " + annotation.ignore());
            } else {
                for (var before : before) {
                    before.invoke(instance);
                }

                var start = System.currentTimeMillis();
                var stop = System.currentTimeMillis();
                Throwable exception = null;
                try {
                    test.invoke(instance);
                    stop = System.currentTimeMillis();
                } catch (InvocationTargetException e){
                    exception = e.getTargetException();
                }
                if (exception != null && !exception.getClass().equals(annotation.expected())) {
                    System.out.println("failed: \"" + test.getName() + "\" " + exception + " " + annotation.expected());
                } else if (exception == null && !annotation.expected().equals(Object.class)) {
                    System.out.println("failed: \"" + test.getName() + "\" expected an exception " + annotation.expected().getName());
                } else {
                    System.out.println("finished: \"" + test.getName() + "\" in " + (stop - start) + " ms");
                }

                for (var after : after) {
                    after.invoke(instance);
                }
            }
        }

        for (var afterClassMethod : afterClass) {
            afterClassMethod.invoke(instance);
        }
    }
}