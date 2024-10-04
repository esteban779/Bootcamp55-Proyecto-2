package com.xyzbank.TransactionMs.service;

import com.xyzbank.TransactionMs.model.Transaccion;

import java.util.List;

public interface TransaccionesService {
    Transaccion deposito(Transaccion transaccion);
    Transaccion retiro(Transaccion transaccion);
    Transaccion transferencia(Transaccion transaccion);
    List<Transaccion> historial();
}
