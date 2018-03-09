package ru.spbau.solikov.threadpool;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implementation of thread pool pattern with fixed number of threads.
 */
public class ThreadPoolImpl<T> {

    private Thread[] pool;
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Sets the number of threads in pool and enables them.
     *
     * @param n number of threads
     */
    public ThreadPoolImpl(int n) {
        pool = new Thread[n];

        for (int i = 0; i < n; i++) {
            pool[i] = new Thread(new ThreadBody());
            pool[i].start();
        }
    }

    /**
     * Kills all threads.
     */
    public void shutdown() {
        for (Thread thread : pool) {
            thread.interrupt();
        }
    }

    public LightFuture<T> addTask(Supplier<T> task) {
        Task newTask = new Task(task);

        synchronized (tasks) {
            tasks.add(newTask);
            tasks.notify();
        }

        return newTask;
    }

    /**
     * Body for threads in thread pool that will be waiting for new tasks and do them.
     */
    private class ThreadBody implements Runnable {

        @Override
        public void run() {
            Task currentTask;

            try {
                while (!Thread.interrupted()) {
                    synchronized (tasks) {
                        while (tasks.size() == 0) {
                            tasks.wait();
                        }

                        currentTask = tasks.remove(0);

                        currentTask.start();
                    }
                }
            } catch (InterruptedException | LightExecutionException ignored) {
            }
        }
    }

    /**
     * Implementation of light future for the thread pool.
     */
    private class Task implements LightFuture<T> {

        private boolean isReady = false;
        private T answer = null;
        private Supplier<T> task;
        private Exception exception = null;

        /**
         * Constructs light future task by given supplier task
         *
         * @param task task to be constructed from
         */
        public Task(@NotNull Supplier<T> task) {
            this.task = task;
        }

        private synchronized void start() throws LightExecutionException {
            try {
                answer = task.get();
            } catch (Exception e){
                exception = new LightExecutionException();
            }

            isReady = true;

            notifyAll();
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public synchronized T get() throws Exception {
            while (!isReady) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new LightExecutionException();
                }
            }

            if (exception != null){
                throw exception;
            }

            return answer;
        }

        @Override
        public LightFuture<T> thenApply(@NotNull Function<T, T> newTask) {
            return addTask(() -> {
                try {
                    return newTask.apply(get());
                } catch (Exception e) {
                    throw new RuntimeException(e.getCause());
                }
            });
        }
    }
}
