package com.xyzbank.AccountMs.controller;

import com.xyzbank.AccountMs.service.CuentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomClientController {

    @Autowired
    public CuentasService cuentasService;

    @GetMapping(value = "cuentas/countByClientId/{clientId}")
    public ResponseEntity<Long> accountsByClientId(@PathVariable Long clientId) {
        long nroCuentas = cuentasService.accountsByClientId(clientId);
        return ResponseEntity.ok(nroCuentas);
    }
}
