package com.discount.configuration;

import com.discount.exception.BusinessException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 500) {
            log.error("Server error occurred, status: {}, method: {}, and message: {}", response.status(),
                    methodKey, response.body());
            return defaultDecoder.decode(methodKey, response);
        }
        log.warn("Client error occurred, status: {}, method: {}, message: {}",
                response.status(), methodKey, response.body());
        return new BusinessException(
                "Failed to process request %s".formatted(response.body()), response.status());
    }
}
