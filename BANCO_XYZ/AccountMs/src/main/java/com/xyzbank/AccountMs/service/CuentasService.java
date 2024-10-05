package com.xyzbank.AccountMs.service;

import com.xyzbank.AccountMs.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentasService {
    Cuenta crearCuenta(Cuenta cuenta);
    List<Cuenta> listarCuentas();
    Optional<Cuenta> getCuentaPorId(Long id);
    void eliminarCuenta(Long id);
    void depositar(Long cuentaId, Double monto, Boolean isTransfer);
    void retirar(Long cuentaId, Double monto, Boolean isTransfer);
    void transferir(Long origenId, Long destinoId, Double monto);

    long accountsByClientId(Long clientId);
}
