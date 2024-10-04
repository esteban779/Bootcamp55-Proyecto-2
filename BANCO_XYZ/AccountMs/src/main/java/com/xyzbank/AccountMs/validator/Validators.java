package com.xyzbank.AccountMs.validator;

public class Validators {

    public static CuentasValidator cuentaAhorrosValidator = (saldo, montoRetiro) -> {
        if (saldo - montoRetiro < 0) {
            throw new IllegalArgumentException("Saldo insuficiente. El saldo no puede quedar en negativo.");
        }
    };

    public static CuentasValidator cuentaCorrienteValidator = (saldo, montoRetiro) -> {
        if (saldo - montoRetiro <  -500) {
            throw new IllegalArgumentException("Límite de sobregiro excedido. El máximo sobregiro permitido es $500.");
        }
    };
}
