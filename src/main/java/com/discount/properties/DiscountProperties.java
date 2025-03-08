package com.discount.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "discounts")
@Data
public class DiscountProperties {
    private Map<String, BigDecimal> rates;
    private int customerTenureThreshold;
}
