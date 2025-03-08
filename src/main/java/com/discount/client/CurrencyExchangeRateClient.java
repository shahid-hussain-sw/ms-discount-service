package com.discount.client;

import com.discount.response.CurrencyExchangeRateResponse;
import com.discount.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "currencyExchange",
        url = "${feign.client.host}/${feign.client.apiKey}",
        configuration = FeignConfiguration.class)
public interface CurrencyExchangeRateClient {

    @GetMapping(value = "/pair/{baseCode}/{targetCode}")
    CurrencyExchangeRateResponse getExchangeRate(
            @PathVariable String baseCode, @PathVariable String targetCode);
}
