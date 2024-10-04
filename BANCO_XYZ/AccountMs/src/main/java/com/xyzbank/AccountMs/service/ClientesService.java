package com.xyzbank.AccountMs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

@Service
public class ClientesService {

    @Autowired
    private RestTemplate restTemplate;

    public Function<Long, Boolean> clientExists = (clienteId) -> {
        final String url = "http://localhost:1010/clientes/" + clienteId;

        try {
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    };
}
