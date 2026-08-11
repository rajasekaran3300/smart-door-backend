package com.smartdoor.security.validation;

import com.smartdoor.security.dto.request.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequest> {
    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getConfirmPassword() == null) {
            return true; // let @NotBlank handle nulls
        }
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
