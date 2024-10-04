package com.xyzbank.AccountMs.entity;

import com.xyzbank.AccountMs.model.Cuenta;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cuentas")
public class CuentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;

    @Column(name = "saldo", nullable = false)
    private double saldo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private Cuenta.TipoCuentaEnum tipoCuenta;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;
}
