package org.miage.kirbach.bankservice.boundary;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler
    ProblemDetail problemDetail(IllegalStateException ise) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        pd.setDetail(ise.getLocalizedMessage());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail problemDetail(MethodArgumentNotValidException mnve) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        mnve.getBindingResult().getFieldErrors().forEach(e -> {
            pd.setDetail(e.getField() + " " + e.getDefaultMessage());
        });
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail problemDetail(ConstraintViolationException cve) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        cve.getConstraintViolations().forEach(e -> {
            pd.setDetail(e.getMessage());
        });
        return pd;
    }
}

