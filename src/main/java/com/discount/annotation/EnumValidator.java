package com.discount.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {
    private EnumValue[] enumValues;

    @Override
    public void initialize(ValidateEnum annotation) {
        Class<? extends EnumValue> enumClass = annotation.enumClass();
        enumValues = enumClass.getEnumConstants();
    }

    /**
     * Validate enum
     *
     * @param value   the enum value
     * @param context the constraint validator context
     * @return true | false if enum is valid
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return Arrays.stream(enumValues).anyMatch(e -> e.value().equalsIgnoreCase(value));
    }
}
