package org.miage.kirbach.BankService.Entity;

import java.util.Set;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class DemandeCreditValidator {

    private final Validator validator;

    DemandeCreditValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(DemandeCreditInput demandeCredit) {
        Set<ConstraintViolation<DemandeCreditInput>> violations = validator.validate(demandeCredit);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
