package com.discount.response;

import java.math.BigDecimal;

public record DiscountResponse(BigDecimal netPayableAmount, String targetCurrency) {
}
