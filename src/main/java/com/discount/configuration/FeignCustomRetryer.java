package com.discount.configuration;

import com.discount.exception.BusinessException;
import feign.RetryableException;
import feign.Retryer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class FeignCustomRetryer implements feign.Retryer {
    private final Integer maxAttempts;
    private final Long sleepTime;
    private int attempt = 1;

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (attempt >= maxAttempts) {
            log.error(
                    "Max retry attempts {} exceeded. Unable to complete the request. Error: {}",
                    maxAttempts,
                    e.getMessage(),
                    e);
            throw new BusinessException(
                    "Failed to process currency conversion after "
                            + maxAttempts
                            + " retry attempts. Reason: "
                            + e.getMessage(),
                    e.status());
        }
        attempt++;

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            log.error("Thread interrupted while waiting for retry. Error: {}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
            throw new BusinessException(
                    "Failed to process currency conversion due to thread interruption. Reason: "
                            + ex.getMessage(),
                    e.status());
        }
    }

    @Override
    public Retryer clone() {
        return new FeignCustomRetryer(maxAttempts, sleepTime);
    }
}
