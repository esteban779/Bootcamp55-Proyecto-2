package com.xyzbank.AccountMs.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransaccionDTO {
    private String id;
    private String tipo;
    private BigDecimal monto;
    private String fecha;
    private String cuentas;

    public TransaccionDTO() {

    }

    public TransaccionDTO(String tipo, BigDecimal monto, String fecha, String cuentas) {
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
        this.cuentas = cuentas;
    }
}
