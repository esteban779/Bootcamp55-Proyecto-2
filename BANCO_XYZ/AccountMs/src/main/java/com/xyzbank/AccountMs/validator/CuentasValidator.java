package com.xyzbank.AccountMs.validator;

@FunctionalInterface
public interface CuentasValidator {
    void validate(double saldo, double montoRetiro);
}
