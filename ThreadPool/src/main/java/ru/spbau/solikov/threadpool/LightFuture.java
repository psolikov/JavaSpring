package ru.spbau.solikov.threadpool;

import java.util.function.Function;

/**
 * Represents a task for ThreadPoolImpl.
 *
 * @param <T> type of task
 */
public interface LightFuture<T> {

    /**
     * Checks the state of the task.
     *
     * @return true if the task has been already executed or false if not
     */
    boolean isReady();

    /**
     * Requests the result of the task. If the result has not been evaluated yet,
     * waits for it and returns it afterwards.
     *
     * @return evaluated answer
     * @throws LightExecutionException if corresponding Supplier throws an exception
     */
    T get() throws LightExecutionException;

    /**
     * Represents composition of tasks.
     *
     * @param newTask takes result of task and returns new task
     * @return new task
     */
    LightFuture<T> thenApply(Function<T, T> newTask);
}
