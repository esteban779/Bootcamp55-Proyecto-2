package com.xyzbank.TransactionMs.service;

import com.xyzbank.TransactionMs.entity.TransaccionEntity;
import com.xyzbank.TransactionMs.model.Transaccion;
import com.xyzbank.TransactionMs.repository.TransaccionesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TransaccionesServiceImpl implements TransaccionesService {

    @Autowired
    private ModelMapper modelMapper;

    private final TransaccionesRepository transaccionesRepository;

    public TransaccionesServiceImpl(TransaccionesRepository transaccionesRepository) {
        this.transaccionesRepository = transaccionesRepository;
    }

    @Override
    public Transaccion deposito(Transaccion transaccion) {
        transaccion.setTipo("DEPOSITO");

        try {
            TransaccionEntity transaccionEntity = modelMapper.map(transaccion, TransaccionEntity.class);
            TransaccionEntity newDeposito = transaccionesRepository.insert(transaccionEntity);
            return modelMapper.map(newDeposito, Transaccion.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public Transaccion retiro(Transaccion transaccion) {
        transaccion.setTipo("RETIRO");
        try {
            TransaccionEntity transaccionEntity = modelMapper.map(transaccion, TransaccionEntity.class);
            TransaccionEntity newRetiro = transaccionesRepository.insert(transaccionEntity);
            return modelMapper.map(newRetiro, Transaccion.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public Transaccion transferencia(Transaccion transaccion) {
        transaccion.setTipo("TRANSFERENCIA");
        try {
            TransaccionEntity transaccionEntity = modelMapper.map(transaccion, TransaccionEntity.class);
            TransaccionEntity newTransferencia = transaccionesRepository.insert(transaccionEntity);
            return modelMapper.map(newTransferencia, Transaccion.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public List<Transaccion> historial() {
        List<TransaccionEntity> transacciones = transaccionesRepository.findAll();
        return transacciones
                .stream()
                .map(t -> modelMapper.map(t, Transaccion.class))
                .sorted(Comparator.comparing(Transaccion::getTipo))
                .toList();
    }
}
