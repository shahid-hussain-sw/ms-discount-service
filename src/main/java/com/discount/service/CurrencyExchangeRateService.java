package com.discount.service;

import java.math.BigDecimal;

public interface CurrencyExchangeRateService {

    BigDecimal getExchangeRate(String baseCurrency, String targetCurrency);
}
