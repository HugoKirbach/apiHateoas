package org.miage.kirbach.BankService.Loan.Entity;

import java.util.Set;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class LoanValidator {

    private final Validator validator;

    LoanValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(LoanInput loan) {
        Set<ConstraintViolation<LoanInput>> violations = validator.validate(loan);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
