package ru.spbau.solikov.threadpool.test;

import org.junit.Test;
import ru.spbau.solikov.threadpool.LightExecutionException;
import ru.spbau.solikov.threadpool.LightFuture;
import ru.spbau.solikov.threadpool.ThreadPoolImpl;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests for implementation of thread pool.
 */
public class ThreadPoolImplTest {

    @Test
    public void testNotReady() {
        ThreadPoolImpl<Integer> threadPool = new ThreadPoolImpl<>(5);

        LightFuture task = threadPool.addTask(() -> 0);

        assertEquals(task.isReady(), false);
    }

    @Test
    public void testIsReady() throws LightExecutionException {
        ThreadPoolImpl<Integer> threadPool = new ThreadPoolImpl<>(5);

        LightFuture task = threadPool.addTask(() -> 0);

        task.get();

        assertEquals(task.isReady(), true);
    }

    @Test
    public void testThenApply() throws LightExecutionException {
        ThreadPoolImpl<Integer> threadPool = new ThreadPoolImpl<>(5);

        LightFuture<Integer> task = threadPool.addTask(() -> 1).thenApply(x -> x + 2)
                .thenApply(x -> x + 3).thenApply(x -> x + 4).thenApply(x -> x / 2);

        assertEquals(task.get(), Integer.valueOf (5));
    }

    @Test
    public void testAddTask() throws LightExecutionException {
        ThreadPoolImpl<Integer> threadPool = new ThreadPoolImpl<>(10);

        LightFuture<Integer> task1 = threadPool.addTask(() -> (1231 * 32) / 2);
        LightFuture<Integer> task2 = threadPool.addTask(() -> 10 * 10);
        LightFuture<Integer> task3 = threadPool.addTask(() -> 1 + 2 + 3 + 4);

        assertEquals(task1.get(), Integer.valueOf(19696));
        assertEquals(task2.get(), Integer.valueOf(100));
        assertEquals(task3.get(), Integer.valueOf(10));

        threadPool.shutdown();
    }

    @Test
    public void testOneThreadManyTasks() throws LightExecutionException {
        ThreadPoolImpl<Integer> threadPool = new ThreadPoolImpl<>(1);

        ArrayList<LightFuture<Integer>> arrayList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final int localI = i + 100;
            arrayList.add(threadPool.addTask(() -> localI * localI));
        }

        for (int i = 0; i < 10; i++){
            assertEquals(arrayList.get(i).get(), Integer.valueOf((i + 100) * (i + 100)));
        }

        threadPool.shutdown();
    }

    @Test
    public void testManyThreadsManyTasks() throws LightExecutionException {
        ThreadPoolImpl<Integer> threadPool = new ThreadPoolImpl<>(50);

        ArrayList<LightFuture<Integer>> arrayList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            final int localI = i + 100;
            arrayList.add(threadPool.addTask(() -> localI * localI));
        }

        for (int i = 0; i < 100; i++){
            assertEquals(arrayList.get(i).get(), Integer.valueOf((i + 100) * (i + 100)));
        }

        threadPool.shutdown();
    }

    @Test(expected = LightExecutionException.class)
    public void testExceptionInGet() throws LightExecutionException {
        ThreadPoolImpl<Void> threadPool = new ThreadPoolImpl<>(5);

        LightFuture<Void> task = threadPool.addTask(() -> {
            throw new RuntimeException("RE");
        });

        task.get();

        threadPool.shutdown();
    }
}