package ru.spbau.solikov.lazy;

/**
 * Represents lazy evaluations.
 * @param <T> type of evaluated value
 */
public interface Lazy<T> {

    /**
     * Evaluates answer and returns it.
     * Works only 1 time and after that just returns the answer.
     * @return answer
     */
    T get();
}