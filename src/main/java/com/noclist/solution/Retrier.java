package com.noclist.solution;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.util.Assert;

/**
 * This class tries to get the result out of the supplier several times
 */
public class Retrier {
    private final int maxAttempts;
    private final long sleepTimeMillis;

    public Retrier(int maxAttempts, long sleepTimeMillis) {
        Assert.isTrue(maxAttempts >= 0, "Number of max attempts is below zero");
        Assert.isTrue(sleepTimeMillis >= 0, "Sleep time is below zero");
        this.maxAttempts = maxAttempts;
        this.sleepTimeMillis = sleepTimeMillis;
    }

    /**
     * Supplier is called multiple times until a non empty optional is returned (maximum number of calls = maxAttempts)
     * Thread is suspended for sleepTimeMillis between the tries.
     * @param supplier provides the result
     * @param <T> result type
     * @return Optional with the result if the number of tries gives some result, an empty optional otherwise.
     * @throws InterruptedException if the execution thread has been interrupted.
     */
    public <T> Optional<T> retry(Supplier<Optional<T>> supplier) throws InterruptedException {
        int attempts = maxAttempts;

        while (attempts > 0) {
            attempts--;

            try {
                Optional<T> result = supplier.get();
                if (result.isPresent()) {
                    return result;
                }
            } catch(Exception e) {
                System.err.println("Supplier " + supplier + " produced exception: " + e.getLocalizedMessage());
            }
            if (attempts > 0) {
                Thread.sleep(sleepTimeMillis);
            }
        }
        return Optional.empty();
    }
}
