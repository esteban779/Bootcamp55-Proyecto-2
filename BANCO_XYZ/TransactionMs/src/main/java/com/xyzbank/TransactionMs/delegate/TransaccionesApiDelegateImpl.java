package com.xyzbank.TransactionMs.delegate;

import com.xyzbank.TransactionMs.api.TransaccionesApiDelegate;
import com.xyzbank.TransactionMs.model.Transaccion;
import com.xyzbank.TransactionMs.service.TransaccionesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransaccionesApiDelegateImpl implements TransaccionesApiDelegate {

    private final TransaccionesService transaccionesService;

    public TransaccionesApiDelegateImpl(TransaccionesService transaccionesService) {
        this.transaccionesService = transaccionesService;
    }

    @Override
    public ResponseEntity<Transaccion> deposito(Transaccion transaccion) {
        Transaccion newDeposito = transaccionesService.deposito(transaccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDeposito);
    }

    @Override
    public ResponseEntity<Transaccion> retiro(Transaccion transaccion) {
        Transaccion newRetiro = transaccionesService.retiro(transaccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRetiro);
    }

    @Override
    public ResponseEntity<Transaccion> transferencia(Transaccion transaccion) {
        Transaccion newTransferencia = transaccionesService.transferencia(transaccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransferencia);
    }

    @Override
    public ResponseEntity<List<Transaccion>> historial() {
        List<Transaccion> transacciones = transaccionesService.historial();
        return new ResponseEntity<>(transacciones, HttpStatus.OK);
    }
}
