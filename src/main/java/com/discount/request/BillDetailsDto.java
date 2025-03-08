package com.discount.request;

import com.discount.annotation.ValidateEnum;
import com.discount.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Bill details containing details for calculation")
public record BillDetailsDto(
        @NotNull(message = "Items cannot be null")
        @Size(min = 1, message = "Items should have at least 1 item")
        @Valid
        List<ItemDto> items,
        @NotNull(message = "Total amount cannot be null")
        @Positive(message = "Total should be greater then 0")
        BigDecimal totalAmount,
        @NotBlank(message = "User type cannot be null or empty")
        @ValidateEnum(enumClass = UserType.class, message = "User type value is invalid")
        String userType,
        @NotNull(message = "Customer tenure cannot be null")
        @PositiveOrZero(message = "Customer tenure value cannot be less then 0")
        Integer customerTenure,
        @NotBlank(message = "Original currency cannot be null or empty") String originalCurrency,
        @NotBlank(message = "Target currency cannot be null or empty") String targetCurrency) {
}
