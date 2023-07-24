package com.svetomsk.crudtransactions.model;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateCashDeskRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void emptyBalance_violation() {
        var errors = validator.validate(new CreateCashDeskRequest()).size();
        assertEquals(errors, 1);
    }

    @Test
    public void negativeBalance_violation() {
        var errors = validator.validate(CreateCashDeskRequest.builder().initialBalance(-10.0).build()).size();
        assertEquals(errors, 1);
    }

    @Test
    public void nonNegativeBalance_ok() {
        var errors = validator.validate(CreateCashDeskRequest.builder().initialBalance(0.0).build()).size();
        assertEquals(errors, 0);
        errors = validator.validate(CreateCashDeskRequest.builder().initialBalance(10.0).build()).size();
        assertEquals(errors, 0);
    }

}