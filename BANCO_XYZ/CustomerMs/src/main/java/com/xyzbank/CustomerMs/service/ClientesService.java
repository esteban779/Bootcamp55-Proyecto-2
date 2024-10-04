package com.xyzbank.CustomerMs.service;

import com.xyzbank.CustomerMs.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClientesService {
    Cliente crearCliente(Cliente cliente);
    List<Cliente> listarClientes();
    Optional<Cliente> getClientePorId(Long id);
    Cliente actualizarCliente(Long id, Cliente cliente);
    void eliminarCliente(Long id);
}
