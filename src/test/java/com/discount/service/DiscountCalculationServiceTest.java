package com.discount.service;

import com.discount.exception.BusinessException;
import com.discount.properties.DiscountProperties;
import com.discount.request.BillDetailsDto;
import com.discount.request.ItemDto;
import com.discount.response.DiscountResponse;
import com.discount.service.impl.DiscountCalculationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DiscountCalculationServiceTest {
    @Mock
    private CurrencyExchangeRateService currencyExchangeService;

    @Mock
    private DiscountProperties discountProperties;

    @InjectMocks
    private DiscountCalculationServiceImpl discountCalculationService;

    private BillDetailsDto billDetails;

    @BeforeEach
    void setUp() {
        billDetails = new BillDetailsDto(
                List.of(new ItemDto("Item1", new BigDecimal("100"), "Grocery"),
                        new ItemDto("Item2", new BigDecimal("200"), "Non-Grocery")),
                new BigDecimal("300"),
                "CUSTOMER",
                3,
                "USD",
                "EUR"
        );
    }

    @Test
    void testCalculateAndApplyDiscount_WhenCustomerIsEmployee() {
        billDetails = new BillDetailsDto(
                billDetails.items(),
                billDetails.totalAmount(),
                "EMPLOYEE",
                billDetails.customerTenure(),
                billDetails.originalCurrency(),
                billDetails.targetCurrency()
        );
        when(discountProperties.getRates()).thenReturn(Map.of("EMPLOYEE", new BigDecimal("0.30")));
        when(currencyExchangeService.getExchangeRate("USD", "EUR")).thenReturn(new BigDecimal("0.85"));
        DiscountResponse response = discountCalculationService.calculateAndApplyDiscount(billDetails);
        assertNotNull(response);
        assertEquals(new BigDecimal("195.50"), response.netPayableAmount());
        assertEquals("EUR", response.targetCurrency());
    }

    @Test
    void testCalculateAndApplyDiscount_WhenCustomerIsAffiliate() {
        billDetails = new BillDetailsDto(
                billDetails.items(),
                billDetails.totalAmount(),
                "AFFILIATE",
                billDetails.customerTenure(),
                billDetails.originalCurrency(),
                billDetails.targetCurrency()
        );
        when(discountProperties.getRates()).thenReturn(Map.of("AFFILIATE", new BigDecimal("0.10")));
        when(currencyExchangeService.getExchangeRate("USD", "EUR")).thenReturn(new BigDecimal("0.85"));
        DiscountResponse response = discountCalculationService.calculateAndApplyDiscount(billDetails);
        assertNotNull(response);
        assertEquals(new BigDecimal("229.50"), response.netPayableAmount());
        assertEquals("EUR", response.targetCurrency());
    }

    @Test
    void testCalculateAndApplyDiscount_CustomerIsLongTerm() {
        billDetails = new BillDetailsDto(
                billDetails.items(),
                billDetails.totalAmount(),
                "CUSTOMER",
                3,
                billDetails.originalCurrency(),
                billDetails.targetCurrency()
        );
        when(discountProperties.getRates()).thenReturn(Map.of("CUSTOMER", new BigDecimal("0.05")));
        when(discountProperties.getCustomerTenureThreshold()).thenReturn(2);
        when(currencyExchangeService.getExchangeRate("USD", "EUR")).thenReturn(new BigDecimal("0.85"));
        DiscountResponse response = discountCalculationService.calculateAndApplyDiscount(billDetails);
        assertNotNull(response);
        assertEquals(new BigDecimal("238.00"), response.netPayableAmount());
        assertEquals("EUR", response.targetCurrency());
    }

    @Test
    void testCalculateAndApplyDiscount_CustomerIsShortTerm() {
        billDetails = new BillDetailsDto(
                billDetails.items(),
                billDetails.totalAmount(),
                "CUSTOMER",
                1,
                billDetails.originalCurrency(),
                billDetails.targetCurrency()
        );

        when(discountProperties.getRates()).thenReturn(Map.of("CUSTOMER", new BigDecimal("0.05"), "DEFAULT", new BigDecimal("0.00")));
        when(discountProperties.getCustomerTenureThreshold()).thenReturn(2);
        when(currencyExchangeService.getExchangeRate("USD", "EUR")).thenReturn(new BigDecimal("0.85"));
        DiscountResponse response = discountCalculationService.calculateAndApplyDiscount(billDetails);
        assertNotNull(response);
        assertEquals(new BigDecimal("242.25"), response.netPayableAmount());
        assertEquals("EUR", response.targetCurrency());
    }

    @Test
    void testCalculateAndApplyDiscount_DefaultDiscount() {
        billDetails = new BillDetailsDto(
                billDetails.items(),
                billDetails.totalAmount(),
                "GUEST",
                billDetails.customerTenure(),
                billDetails.originalCurrency(),
                billDetails.targetCurrency()
        );
        when(discountProperties.getRates()).thenReturn(Map.of(
                "DEFAULT", new BigDecimal("0.02")
        ));
        when(currencyExchangeService.getExchangeRate("USD", "EUR")).thenReturn(new BigDecimal("0.85"));
        DiscountResponse response = discountCalculationService.calculateAndApplyDiscount(billDetails);
        assertNotNull(response);
        assertEquals(new BigDecimal("243.10"), response.netPayableAmount());
        assertEquals("EUR", response.targetCurrency());
    }

    @Test
    void testCalculateAndApplyDiscount_WhenTotalAmountMismatchException() {
        billDetails = new BillDetailsDto(
                List.of(new ItemDto("Item1", new BigDecimal("100"), "Grocery"),
                        new ItemDto("Item2", new BigDecimal("200"), "Non-Grocery")),
                new BigDecimal("400"),
                "CUSTOMER",
                3,
                "USD",
                "EUR"
        );

        BusinessException exception = assertThrows(BusinessException.class, () -> discountCalculationService.calculateAndApplyDiscount(billDetails));
        assertEquals("Failed to calculate discount for user: CUSTOMER, reason: Total amount is different from Item total amount", exception.getMessage());
        assertEquals(400, exception.getStatus());
    }

    @Test
    void testCalculateAndApplyDiscount_ThrowCurrencyExchangeException() {
        when(discountProperties.getRates()).thenReturn(Map.of("CUSTOMER", new BigDecimal("0.05")));
        when(discountProperties.getCustomerTenureThreshold()).thenReturn(2);
        when(currencyExchangeService.getExchangeRate("USD", "EUR")).thenThrow(new BusinessException("Exchange rate not found", 404));
        BusinessException exception = assertThrows(BusinessException.class, () -> discountCalculationService.calculateAndApplyDiscount(billDetails));
        assertEquals("Failed to calculate discount for user: CUSTOMER, reason: Exchange rate not found", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }
}