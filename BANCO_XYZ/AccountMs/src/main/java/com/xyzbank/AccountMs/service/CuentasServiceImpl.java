package com.xyzbank.AccountMs.service;

import com.xyzbank.AccountMs.entity.CuentaEntity;
import com.xyzbank.AccountMs.exception.ClientNotFoundException;
import com.xyzbank.AccountMs.model.Cuenta;
import com.xyzbank.AccountMs.model.TransaccionDTO;
import com.xyzbank.AccountMs.repository.CuentasRepository;
import com.xyzbank.AccountMs.validator.CuentasValidator;
import com.xyzbank.AccountMs.validator.Validators;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
public class CuentasServiceImpl implements CuentasService {

    private final CuentasRepository cuentasRepository;
    private final ClientesService clientesService;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    public CuentasServiceImpl(CuentasRepository cuentasRepository,
                              ClientesService clientesService,
                              ModelMapper modelMapper,
                              RestTemplate restTemplate
    ) {
        this.cuentasRepository = cuentasRepository;
        this.clientesService = clientesService;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public Cuenta crearCuenta(Cuenta cuenta) {
        boolean clientExists = clientesService.clientExists.apply(cuenta.getClienteId());

        if (!clientExists) {
            throw new ClientNotFoundException("Cliente con ID " + cuenta.getClienteId() + " no existe.");
        }

        if (cuenta.getSaldo() <= 0) {
            throw new IllegalArgumentException("El saldo inicial debe ser mayor a 0.");
        }

        String numeroCuenta = RandomStringUtils.randomNumeric(15);
        cuenta.setNumeroCuenta(numeroCuenta);

        try {
            CuentaEntity cuentaEntity = modelMapper.map(cuenta, CuentaEntity.class);
            CuentaEntity newAccount = cuentasRepository.save(cuentaEntity);
            return modelMapper.map(newAccount, Cuenta.class);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("El número de cuenta ya se encuentra registrado.");
        }
    }

    @Override
    public List<Cuenta> listarCuentas() {
        try {
            List<CuentaEntity> cuentas = cuentasRepository.findAll();
            return cuentas
                    .stream()
                    .map(cuenta -> modelMapper.map(cuenta, Cuenta.class))
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("No se encontraron cuentas registradas.");
        }
    }

    @Override
    public Optional<Cuenta> getCuentaPorId(Long id) {
        CuentaEntity cuenta = cuentasRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró una cuenta bancaria con el ID buscado."));

        Cuenta cuentaData = modelMapper.map(cuenta, Cuenta.class);

        return Optional.of(cuentaData);
    }

    @Override
    public void eliminarCuenta(Long id) {
        CuentaEntity cuenta = cuentasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró una cuenta bancaria con el ID buscado."));

        if (cuenta.getSaldo() != 0) {
            throw new IllegalArgumentException("La cuenta aún tiene un saldo de " + cuenta.getSaldo());
        }

        cuentasRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void depositar(Long cuentaId, Double monto, Boolean isTransfer) {
        CuentaEntity cuenta = cuentasRepository.findById(cuentaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la cuenta."));

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a depositar no puede ser 0 o negativo.");
        }

        BigDecimal newSaldo = BigDecimal.valueOf(cuenta.getSaldo()).add(BigDecimal.valueOf(monto));
        cuenta.setSaldo(newSaldo.setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        cuentasRepository.save(cuenta);

        if (!isTransfer) {
            transactionCall("deposito", monto, cuenta.getNumeroCuenta());
        }
    }

    @Override
    @Transactional
    public void retirar(Long cuentaId, Double monto, Boolean isTransfer) {
        CuentasValidator validator;

        CuentaEntity cuenta = cuentasRepository.findById(cuentaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la cuenta."));

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a depositar no puede ser 0 o negativo.");
        }

        if (cuenta.getTipoCuenta() == Cuenta.TipoCuentaEnum.AHORROS) {
            validator = Validators.cuentaAhorrosValidator;
        } else {
            validator = Validators.cuentaCorrienteValidator;
        }

        validator.validate(cuenta.getSaldo(), monto);
        BigDecimal newSaldo = BigDecimal.valueOf(cuenta.getSaldo()).subtract(BigDecimal.valueOf(monto));
        cuenta.setSaldo(newSaldo.setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        cuentasRepository.save(cuenta);

        if (!isTransfer) {
            transactionCall("retiro", monto, cuenta.getNumeroCuenta());
        }
    }

    @Override
    @Transactional
    public void transferir(Long origenId, Long destinoId, Double monto) {
        if (!origenId.equals(destinoId)) {
            retirar(origenId, monto, true);
            depositar(destinoId, monto, true);
        } else {
            throw new IllegalArgumentException("Las cuentas de origen y destino no pueden ser las mismas.");
        }

        String cuentaDestino = cuentasRepository.findById(destinoId).map(CuentaEntity::getNumeroCuenta).get();

        transactionCall("transferencia", monto, cuentaDestino);
    }

    @Override
    public long accountsByClientId(Long clientId) {
        return cuentasRepository.countByClienteId(clientId);
    }

    private void transactionCall(String tipo, Double monto, String numeroCuenta) {
        TransaccionDTO transaccion = new TransaccionDTO(
                tipo.toUpperCase(),
                BigDecimal.valueOf(monto),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                numeroCuenta
        );

        String urlDeposito = "http://localhost:1050/transacciones/" + tipo;

        restTemplate.postForEntity(urlDeposito, transaccion, TransaccionDTO.class);
    }
}
