package com.discount.service.impl;

import com.discount.client.CurrencyExchangeRateClient;
import com.discount.response.CurrencyExchangeRateResponse;
import com.discount.service.CurrencyExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {

    private final CurrencyExchangeRateClient currencyExchangeClient;

    @Override
    @Cacheable(value = "exchangeRates", key = "#baseCurrency + '-' + #targetCurrency")
    public BigDecimal getExchangeRate(String baseCurrency, String targetCurrency) {
        CurrencyExchangeRateResponse clientResponse =
                currencyExchangeClient.getExchangeRate(baseCurrency, targetCurrency);
        return clientResponse.conversionRate();
    }
}
