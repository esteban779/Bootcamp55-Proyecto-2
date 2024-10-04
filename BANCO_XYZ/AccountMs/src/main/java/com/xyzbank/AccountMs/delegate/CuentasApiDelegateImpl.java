package com.xyzbank.AccountMs.delegate;

import com.xyzbank.AccountMs.api.CuentasApiDelegate;
import com.xyzbank.AccountMs.model.Cuenta;
import com.xyzbank.AccountMs.model.ResponseModel;
import com.xyzbank.AccountMs.service.CuentasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CuentasApiDelegateImpl implements CuentasApiDelegate {

    private final CuentasService cuentasService;

    public CuentasApiDelegateImpl(CuentasService cuentasService) {
        this.cuentasService = cuentasService;
    }

    @Override
    public ResponseEntity<Cuenta> crearCuenta(Cuenta cuenta) {
        Cuenta newAccount = cuentasService.crearCuenta(cuenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }

    @Override
    public ResponseEntity<List<Cuenta>> listarCuentas() {
        List<Cuenta> cuentas = cuentasService.listarCuentas();
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Cuenta> getCuentaPorId(Long id) {
        Optional<Cuenta> cuenta = cuentasService.getCuentaPorId(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(cuenta.get());
    }

    @Override
    public ResponseEntity<ResponseModel> eliminarCuenta(Long id) {
        cuentasService.eliminarCuenta(id);

        ResponseModel response = new ResponseModel().code(200).message("Cuenta eliminada correctamente.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> depositar(Long cuentaId, Double monto) {
        cuentasService.depositar(cuentaId, monto);

        ResponseModel response = new ResponseModel().code(200).message("Depositado correctamente: $" + monto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> retirar(Long cuentaId, Double monto) {
        cuentasService.retirar(cuentaId, monto);

        ResponseModel response = new ResponseModel().code(200).message("Se realizó el retiro de $" + monto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
