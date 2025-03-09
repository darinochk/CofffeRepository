package org.example.coffeservice.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private final String PHONE_REGEXP = "^[1-9][\\d]{10}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return phone == null || phone.matches(PHONE_REGEXP);
    }
}
