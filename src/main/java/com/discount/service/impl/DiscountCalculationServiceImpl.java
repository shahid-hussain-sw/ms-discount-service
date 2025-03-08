package com.discount.service.impl;

import com.discount.exception.BusinessException;
import com.discount.properties.DiscountProperties;
import com.discount.request.BillDetailsDto;
import com.discount.response.DiscountResponse;
import com.discount.service.CurrencyExchangeRateService;
import com.discount.service.DiscountCalculationService;
import com.discount.util.DiscountConstant;
import com.discount.util.DiscountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountCalculationServiceImpl implements DiscountCalculationService {
    private final CurrencyExchangeRateService currencyExchangeService;
    private final DiscountProperties discountProperties;

    @Override
    public DiscountResponse calculateAndApplyDiscount(BillDetailsDto billDetails) {
        log.info("Starting discount calculation for bill: {}", billDetails);
        try {
            DiscountResponse discountDto = calculateFinalPayableAmount(billDetails);
            log.info("Discount calculation completed successfully: {}", discountDto);
            return discountDto;
        } catch (BusinessException e) {
            String message = String.format("Failed to calculate discount for user: %s, reason: %s", billDetails.userType(), e.getMessage());
            log.error(message, e);
            throw new BusinessException(message, e.getStatus());
        } catch (Exception e) {
            String message = String.format("Unexpected error occurred while calculating discount for user: %s",
                    billDetails.userType());
            log.error(message, e);
            throw new BusinessException(message, 500);
        }
    }

    private DiscountResponse calculateFinalPayableAmount(BillDetailsDto billDetails) {
        BigDecimal discountedAmount = calculateDiscountedAmount(billDetails);
        log.debug("Discounted amount in original currency: {}", discountedAmount);
        BigDecimal exchangeRate = currencyExchangeService.getExchangeRate(billDetails.originalCurrency(),
                billDetails.targetCurrency());
        log.debug("Exchange rate from {} to {}: {}", billDetails.originalCurrency(), billDetails.targetCurrency(),
                exchangeRate);
        return new DiscountResponse(discountedAmount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP), billDetails.targetCurrency());
    }

    private BigDecimal calculateDiscountedAmount(BillDetailsDto billDetails) {
        BigDecimal totalAmount = DiscountUtil.calculateTotalAmount(billDetails);
        if (!totalAmount.equals(billDetails.totalAmount())) {
            log.warn("Total amount is different from Item total amount");
            throw new BusinessException("Total amount is different from Item total amount", 400);
        }
        BigDecimal groceryAmount = DiscountUtil.calculateGroceryAmount(billDetails);
        BigDecimal nonGroceryAmount = totalAmount.subtract(groceryAmount);
        BigDecimal percentageDiscount = determineApplicableDiscount(billDetails);
        log.debug("Percentage discount: {}", percentageDiscount);
        BigDecimal amountAfterPercentageDiscount = nonGroceryAmount.multiply(BigDecimal.ONE.subtract(percentageDiscount)).add(groceryAmount);
        log.debug("Amount after percentage discount: {}", amountAfterPercentageDiscount);
        BigDecimal discountMultiplier = amountAfterPercentageDiscount.divideToIntegralValue(new BigDecimal("100"));
        BigDecimal amountBasedDiscount = discountMultiplier.multiply(new BigDecimal("5"));
        log.debug("Amount based discount: {}", amountBasedDiscount);
        BigDecimal finalDiscountedAmount = amountAfterPercentageDiscount.subtract(amountBasedDiscount);
        log.debug("Final discounted amount: {}", finalDiscountedAmount);
        return finalDiscountedAmount.setScale(2, RoundingMode.HALF_UP);

    }

    private BigDecimal determineApplicableDiscount(BillDetailsDto billDetails) {
        String userType = billDetails.userType().toUpperCase();
        int customerTenure = billDetails.customerTenure();
        if (DiscountConstant.CUSTOMER.equalsIgnoreCase(userType) && customerTenure > discountProperties.getCustomerTenureThreshold()) {
            log.debug("Applying 5% discount for long-term customer with tenure: {}", customerTenure);
            return discountProperties.getRates().getOrDefault(userType, discountProperties.getRates().get(DiscountConstant.DEFAULT));
        }else if (DiscountConstant.EMPLOYEE.equalsIgnoreCase(userType)) {
            return discountProperties.getRates().getOrDefault(userType, discountProperties.getRates().get(DiscountConstant.DEFAULT));
        }else if(DiscountConstant.AFFILIATE.equalsIgnoreCase(userType)) {
            return discountProperties.getRates().getOrDefault(userType, discountProperties.getRates().get(DiscountConstant.DEFAULT));
        }
        return discountProperties.getRates().get(DiscountConstant.DEFAULT);

    }
}
