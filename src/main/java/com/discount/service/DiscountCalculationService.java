package com.discount.service;

import com.discount.request.BillDetailsDto;
import com.discount.response.DiscountResponse;

public interface DiscountCalculationService {

    DiscountResponse calculateAndApplyDiscount(BillDetailsDto billDetails);
}
