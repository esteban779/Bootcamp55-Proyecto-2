package com.xyzbank.AccountMs.service;

import com.xyzbank.AccountMs.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentasService {
    Cuenta crearCuenta(Cuenta cuenta);
    List<Cuenta> listarCuentas();
    Optional<Cuenta> getCuentaPorId(Long id);
    void eliminarCuenta(Long id);
    void depositar(Long cuentaId, Double monto);
    void retirar(Long cuentaId, Double monto);

    long accountsByClientId(Long clientId);
}
