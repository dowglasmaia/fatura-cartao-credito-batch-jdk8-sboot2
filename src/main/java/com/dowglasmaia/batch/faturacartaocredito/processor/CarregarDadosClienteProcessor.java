package com.dowglasmaia.batch.faturacartaocredito.processor;

import com.dowglasmaia.batch.faturacartaocredito.dominio.Cliente;
import com.dowglasmaia.batch.faturacartaocredito.dominio.FaturaCartaoCredito;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CarregarDadosClienteProcessor implements ItemProcessor<FaturaCartaoCredito, FaturaCartaoCredito> {

    private static final Logger logger = LoggerFactory.getLogger(CarregarDadosClienteProcessor.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public FaturaCartaoCredito process(FaturaCartaoCredito faturaCartaoCredito) throws Exception {
        int clienteId = faturaCartaoCredito.getCliente().getId();
        String uri = String.format("https://my-json-server.typicode.com/giuliana-bezerra/demo/profile/%s", clienteId);

        logger.info("Fetching client data from URI: {}", uri);

        ResponseEntity<Cliente> response = restTemplate.getForEntity(uri, Cliente.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            logger.error("Client not found for URI: {}", uri);
            throw new ValidationException("Cliente não encontrado!");
        }

        faturaCartaoCredito.setCliente(response.getBody());
        logger.info("Client data loaded successfully for client ID: {}", clienteId);

        return faturaCartaoCredito;
    }
}
