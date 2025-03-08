package com.discount.util;

import com.discount.request.BillDetailsDto;
import com.discount.request.ItemDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
@Slf4j
public class DiscountUtil {

    public static BigDecimal calculateTotalAmount(BillDetailsDto billDetails) {
        return billDetails.items().stream()
                .map(ItemDto::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calculateGroceryAmount(BillDetailsDto billDetails) {
        BigDecimal groceryAmount = billDetails.items().stream()
                .filter(item -> DiscountConstant.GROCERY.equalsIgnoreCase(item.category()))
                .map(ItemDto::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.debug("Grocery amount calculated: {}", groceryAmount);
        return groceryAmount;
    }

}
