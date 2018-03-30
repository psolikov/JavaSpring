package ru.spbau.solikov.lazy.test;

import org.junit.Test;
import ru.spbau.solikov.lazy.Lazy;
import ru.spbau.solikov.lazy.LazyFactory;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Tests for LazyFactory class.
 */
public class LazyTest {

    @Test
    public void testSingleLazyGet() {
        Lazy<Integer> lazy = LazyFactory.createSingleThreadLazy(() -> 10 + 5);

        assertEquals(lazy.get(), Integer.valueOf(15));
    }

    @Test
    public void testSingleThreadLazyWhenSupplierReturnsNull() throws Exception {
        Lazy<Object> lazy = LazyFactory.createSingleThreadLazy(() -> null);

        assertEquals(lazy.get(), null);
    }

    @Test
    public void testSingleThreadLazyRunsOnlyOneTime() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<>();

        Lazy<Integer> lazy = LazyFactory.createMultiThreadLazy(() -> {
            arrayList.add(1);
            return 0;
        });

        for (int i = 0; i < 10; i++) {
            lazy.get();
        }

        assertEquals(arrayList.size(), 1);
        assertEquals(arrayList.get(0), Integer.valueOf(1));
    }

    @Test
    public void testMultiThreadWhenSupplierReturnsNull() {
        Lazy<Integer> lazy = LazyFactory.createMultiThreadLazy(() -> null);

        assertEquals(lazy.get(), null);
        assertEquals(lazy.get(), null);
        assertEquals(lazy.get(), null);
    }

    @Test
    public void testMultiThreadThatRunsOnlyOneTime() throws InterruptedException {
        ArrayList<Integer> arrayList = new ArrayList<>();

        Lazy<Integer> lazy = LazyFactory.createMultiThreadLazy(() -> {
            arrayList.add(1);
            return 0;
        });

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(lazy::get));
            threads.get(i).start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(arrayList.size(), 1);
        assertEquals(arrayList.get(0), Integer.valueOf(1));
    }

    @Test
    public void testMultiThreadLazy() throws Exception {
        ArrayList<Integer> arrayList = new ArrayList<>();

        Lazy<Integer> lazy = LazyFactory.createMultiThreadLazy(() -> {
            arrayList.add(1);
            return 0;
        });

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < 100; j++) lazy.get();
            }));
            threads.get(i).start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(arrayList.size(), 1);
        assertEquals(arrayList.get(0), Integer.valueOf(1));
    }
}