package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.entity.Client;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.IClientRepository;
import com.ClinicaDelCalzado_BackEnd.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> findClientByIdClient(Long idClient) {
        return clientRepository.findByIdClient(idClient);
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

}
