package ru.spbau.solikov.lazy;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Factory for building single-thread and multi-thread Lazy objects.
 */
public class LazyFactory {

    /**
     * Creates single-thread Lazy object.
     *
     * @param supplier body of Lazy object
     * @param <T>      type of returning value
     * @return returning value
     */
    public static <T> Lazy<T> createSingleThreadLazy(@NotNull Supplier<T> supplier) {

        return new Lazy<T>() {

            private boolean triggered = false;
            private T answer = null;

            @Override
            public T get() {
                if (!triggered) {
                    triggered = true;
                    answer = supplier.get();
                }

                return answer;
            }
        };
    }

    /**
     * Creates multi-thread Lazy object.
     *
     * @param supplier body of Lazy object
     * @param <T>      type of returning value
     * @return returning value
     */
    public static @NotNull
    <T> Lazy<T> createMultiThreadLazy(@NotNull Supplier<T> supplier) {

        return new Lazy<T>() {

            private boolean triggered = false;
            private T answer = null;

            @Override
            public T get() {

                if (triggered) {
                    return answer;
                }

                synchronized (this) {
                    if (answer == null) {
                        answer = supplier.get();

                        if (answer == null) {
                            triggered = true;
                        }
                    }
                }

                return answer;
            }
        };
    }
}