package com.xyzbank.TransactionMs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "transacciones")
public class TransaccionEntity {
    @Id
    private String id;
    private String tipo;
    private BigDecimal monto;
    private String fecha;
    private String cuentas;
}
