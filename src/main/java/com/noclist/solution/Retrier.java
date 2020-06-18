package com.noclist.solution;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.util.Assert;

/**
 * This class tries to get the result out of the supplier several times
 */
@SuppressWarnings({"PMD.SystemPrintln","PMD.CommentRequired","PMD.CommentSize","PMD.AvoidCatchingGenericException","PMD.LawOfDemeter","PMD.DataflowAnomalyAnalysis"})
public class Retrier {
    transient private final int maxAttempts;
    transient private final long sleepTimeMillis;

    public Retrier(final int maxAttempts, final long sleepTimeMillis) {
        Assert.isTrue(maxAttempts >= 0, "Number of max attempts is below zero");
        Assert.isTrue(sleepTimeMillis >= 0, "Sleep time is below zero");
        this.maxAttempts = maxAttempts;
        this.sleepTimeMillis = sleepTimeMillis;
    }

    /**
     * Supplier is called multiple times until a non empty optional is returned (maximum number of calls = maxAttempts)
     * Thread is suspended for sleepTimeMillis between the tries.
     * 
     * @param supplier provides the result - essentially body of Lambda
     * @param <T> result type
     * @return Optional with the result if the number of tries gives some result, an empty optional otherwise.
     * @throws InterruptedException if the execution thread has been interrupted.
     */
    public <T> Optional<T> retry(final Supplier<Optional<T>> supplier) throws InterruptedException {
        int attempts = maxAttempts;
        Optional<T> result = Optional.empty();
        
        while (attempts > 0) {
            attempts--;

            try {
                result = supplier.get();
                if (result.isPresent()) {
                    break;
                }
            } catch(Exception e) {
                System.err.println("Supplier " + supplier + " produced exception: " + e.getLocalizedMessage());
            }
            if (attempts > 0) {
                Thread.sleep(sleepTimeMillis);
            }
        }
        return result.isPresent() ? result : Optional.empty();
    }
}
