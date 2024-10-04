package com.xyzbank.CustomerMs.delegate;

import com.xyzbank.CustomerMs.api.ClientesApiDelegate;
import com.xyzbank.CustomerMs.model.Cliente;
import com.xyzbank.CustomerMs.model.ResponseModel;
import com.xyzbank.CustomerMs.service.ClientesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClientesApiDelegateImpl implements ClientesApiDelegate {

    private final ClientesService clientesService;

    public ClientesApiDelegateImpl(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @Override
    public ResponseEntity<Cliente> crearCliente(Cliente cliente) {
        Cliente newClient = clientesService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @Override
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clientesService.listarClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Cliente> getClientePorId(Long id) {
        Optional<Cliente> cliente = clientesService.getClientePorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(cliente.get());
    }

    @Override
    public ResponseEntity<Cliente> actualizarCliente(Long id, Cliente cliente) {
        Cliente updatedClient = clientesService.actualizarCliente(id, cliente);
        return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
    }

    @Override
    public ResponseEntity<ResponseModel> eliminarCliente(Long id) {
        clientesService.eliminarCliente(id);

        ResponseModel response = new ResponseModel().code(200).type("SUCCESS").message("Cliente eliminado correctamente.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
