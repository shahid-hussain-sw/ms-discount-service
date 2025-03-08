package com.discount.enums;

import com.discount.annotation.EnumValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserType implements EnumValue {
    AFFILIATE("AFFILIATE"),
    EMPLOYEE("EMPLOYEE"),
    CUSTOMER("CUSTOMER");

    private final String value;

    @Override
    public String value() {
        return value;
    }
}
