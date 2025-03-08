package com.discount.service;

import com.discount.client.CurrencyExchangeRateClient;
import com.discount.response.CurrencyExchangeRateResponse;
import com.discount.service.impl.CurrencyExchangeRateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateServiceTest {

    @Mock
    private CurrencyExchangeRateClient currencyExchangeClient;

    @InjectMocks
    private CurrencyExchangeRateServiceImpl currencyExchangeService;

    @Test
    void testGetExchangeRate_Success() {
        CurrencyExchangeRateResponse currencyExchangeRateResponse = new CurrencyExchangeRateResponse(null, null, null, null, null, null, null, "USD", "EUR", new BigDecimal("0.85"));

        when(currencyExchangeClient.getExchangeRate("USD", "EUR")).thenReturn(currencyExchangeRateResponse);

        BigDecimal exchangeRate = currencyExchangeService.getExchangeRate("USD", "EUR");

        assertEquals(new BigDecimal("0.85"), exchangeRate);
        verify(currencyExchangeClient, times(1)).getExchangeRate("USD", "EUR");
    }

    @Test
    void testGetExchangeRate_WhenClientThrowsException() {
        when(currencyExchangeClient.getExchangeRate("USD", "EUR")).thenThrow(new RuntimeException("API failure"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> currencyExchangeService.getExchangeRate("USD", "EUR"));
        assertEquals("API failure", exception.getMessage());
        verify(currencyExchangeClient, times(1)).getExchangeRate("USD", "EUR");
    }


}