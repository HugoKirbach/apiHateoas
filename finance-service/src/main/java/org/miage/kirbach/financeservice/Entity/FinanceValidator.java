package org.miage.kirbach.financeservice.Entity;

import java.util.Set;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class FinanceValidator {

    private final Validator validator;

    FinanceValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(FinanceInput finance) {
        Set<ConstraintViolation<FinanceInput>> violations = validator.validate(finance);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
    
}
