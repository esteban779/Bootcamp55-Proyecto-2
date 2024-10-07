package com.xyzbank.AccountMs.service;

import com.xyzbank.AccountMs.entity.CuentaEntity;
import com.xyzbank.AccountMs.model.TransaccionDTO;
import com.xyzbank.AccountMs.repository.CuentasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TransaccionesSaga {

    private final CuentasService cuentasService;
    private final RestTemplate restTemplate;
    private final CuentasRepository cuentasRepository;

    public TransaccionesSaga(CuentasService cuentasService, RestTemplate restTemplate, CuentasRepository cuentasRepository) {
        this.cuentasService = cuentasService;
        this.restTemplate = restTemplate;
        this.cuentasRepository = cuentasRepository;
    }

    @Transactional
    public void depositoSaga(Long cuentaId, Double monto) {

        cuentasService.depositar(cuentaId, monto);

        try {
            transactionCall("deposito", monto, cuentaId);
        } catch (Exception e) {
            compensacionDeposito(cuentaId, monto);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void retiroSaga(Long cuentaId, Double monto) {

        cuentasService.retirar(cuentaId, monto);

        try {
            transactionCall("retiro", monto, cuentaId);
        } catch (Exception e) {
            compensacionRetiro(cuentaId, monto);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void transferenciaSaga(Long origenId, Long destinoId, Double monto) {

        cuentasService.retirar(origenId, monto);
        cuentasService.depositar(destinoId, monto);

        try {
            transactionCall("transferencia", monto, destinoId);
        } catch (Exception e) {
            compensacionRetiro(origenId, monto);
            compensacionDeposito(destinoId, monto);
            throw new RuntimeException(e);
        }
    }

    private void transactionCall(String tipo, Double monto, Long cuentaId) {
        String numeroCuenta = cuentasRepository.findById(cuentaId).map(CuentaEntity::getNumeroCuenta).get();

        TransaccionDTO transaccion = new TransaccionDTO(
                tipo.toUpperCase(),
                BigDecimal.valueOf(monto),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                numeroCuenta
        );

        String urlDeposito = "http://localhost:1050/transacciones/" + tipo;

        restTemplate.postForEntity(urlDeposito, transaccion, TransaccionDTO.class);
    }

    private void compensacionDeposito(Long cuentaId, Double monto) {
        cuentasService.retirar(cuentaId, monto);
    }

    private void compensacionRetiro(Long cuentaId, Double monto) {
        cuentasService.depositar(cuentaId, monto);
    }
}
