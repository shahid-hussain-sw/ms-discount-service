package com.discount.controller;

import com.discount.request.BillDetailsDto;
import com.discount.response.DiscountResponse;
import com.discount.service.DiscountCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Discount Calculation Controller")
@Slf4j
public class DiscountCalculationController {

    private final DiscountCalculationService discountCalculationService;

    @Operation(summary = "Calculate and apply discount")
    @PostMapping("/calculate")
    @ResponseStatus(HttpStatus.OK)
    public DiscountResponse calculateAndApplyDiscount(@Valid @RequestBody BillDetailsDto billDetails) {
        log.info("Started calculating and apply discount billDetails:{}", billDetails);
        DiscountResponse discountResponse = discountCalculationService.calculateAndApplyDiscount(billDetails);
        log.info("Completed calculation and applied discount response:{}", discountResponse);
        return discountResponse;
    }
}
