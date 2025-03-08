package com.discount.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Item details")
public record ItemDto(
        @NotBlank(message = "Name cannot null or empty") String name,
        @NotNull(message = "Amount cannot null or empty") @Positive BigDecimal price,
        @NotBlank(message = "Category cannot null or empty") String category) {
}
