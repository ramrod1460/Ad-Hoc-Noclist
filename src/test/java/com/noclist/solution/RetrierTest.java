package com.noclist.solution;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
* Fix 1
* Test 5
* Test 6 ...try again
* Test 7
* Test 8 made on Master Branch
* Test 9 made on Master Branch
* Test 10 made on Dev Branch
* Test 11 made on Dev Branch
*/
class RetrierTest {

    @Test
    void retryException() throws InterruptedException {
        Retrier retrier = new Retrier(3, 100);
        Optional<Object> result = retrier.retry(() -> {
            throw new IllegalStateException("Something went wrong");
        });
        Assertions.assertFalse(result.isPresent(), "Cannot get results out of exceptions");
    }

    @Test
    void retryLastAttempt() throws InterruptedException {
        Retrier retrier = new Retrier(3, 100);
        AtomicInteger attempt = new AtomicInteger();
        Optional<String> result = retrier.retry(() -> {
            attempt.addAndGet(1);
            if (attempt.get() < 3) {
                return Optional.empty();
            } else {
                return Optional.of("useful info");
            }
        });
        Assertions.assertTrue(result.isPresent(), "Cannot get the correct result");
        Assertions.assertEquals("useful info", result.get(), "Wrong result");
    }

    @Test
    void retryLastAttemptExceptions() throws InterruptedException {
        Retrier retrier = new Retrier(3, 100);
        AtomicInteger attempt = new AtomicInteger();
        Optional<String> result = retrier.retry(() -> {
            attempt.addAndGet(1);
            if (attempt.get() < 3) {
                throw new IllegalStateException("Something went wrong");
            } else {
                return Optional.of("useful info");
            }
        });
        Assertions.assertTrue(result.isPresent(), "Cannot get the correct result");
        Assertions.assertEquals("useful info", result.get(), "Wrong result");
    }

}