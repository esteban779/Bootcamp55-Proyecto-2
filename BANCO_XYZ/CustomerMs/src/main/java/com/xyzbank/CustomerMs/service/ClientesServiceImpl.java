package com.xyzbank.CustomerMs.service;

import com.xyzbank.CustomerMs.entity.ClienteEntity;
import com.xyzbank.CustomerMs.model.Cliente;
import com.xyzbank.CustomerMs.repository.ClientesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientesServiceImpl implements ClientesService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    private final ClientesRepository clientesRepository;

    public ClientesServiceImpl(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    @Override
    public Cliente crearCliente(Cliente cliente) {
        if (cliente.getDni().length()!=8) {
            throw new IllegalArgumentException("El DNI debe contener 8 dígitos.");
        }
        
        try {
            ClienteEntity clienteEntity = modelMapper.map(cliente, ClienteEntity.class);
            ClienteEntity newClient = clientesRepository.save(clienteEntity);
            return modelMapper.map(newClient, Cliente.class);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("El DNI ya se encuentra registrado.");
        }
    }

    @Override
    public List<Cliente> listarClientes() {
        try {
            List<ClienteEntity> clientes = clientesRepository.findAll();
            return clientes
                    .stream()
                    .map(cliente -> modelMapper.map(cliente, Cliente.class))
                    .sorted(Comparator.comparing(Cliente::getApellido))
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("No se encontraron clientes registrados.");
        }
    }

    @Override
    public Optional<Cliente> getClientePorId(Long id) {
        try {
            Optional<ClienteEntity> cliente = clientesRepository.findById(id);
            return cliente.map(c -> modelMapper.map(c, Cliente.class));
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("No se encontró al cliente con el ID buscado.");
        }
    }

    @Override
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        ClienteEntity clientToUpdate = clientesRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró al cliente con el ID " + id));

        clientToUpdate.setNombre(cliente.getNombre() != null ? cliente.getNombre() : clientToUpdate.getNombre());
        clientToUpdate.setApellido(cliente.getApellido() != null ? cliente.getApellido() : clientToUpdate.getApellido());
        clientToUpdate.setDni(cliente.getDni() != null ? cliente.getDni() : clientToUpdate.getDni());
        clientToUpdate.setEmail(cliente.getEmail() != null ? cliente.getEmail() : clientToUpdate.getEmail());

        try {
            ClienteEntity updatedClient = clientesRepository.save(clientToUpdate);
            return modelMapper.map(updatedClient, Cliente.class);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("No se pudo actualizar la información del cliente.");
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("El DNI ya se encuentra registrado.");
        }
    }

    @Override
    public void eliminarCliente(Long id) {
        final String url = "http://localhost:1020/cuentas/countByClientId/";
        ResponseEntity<Long> response = restTemplate.getForEntity(url + id, Long.class);
        long nroCuentas = response.getBody();

        if (nroCuentas > 0) {
            throw new IllegalStateException("El cliente tiene cuentas activas.");
        }

        clientesRepository.deleteById(id);
    }
}
