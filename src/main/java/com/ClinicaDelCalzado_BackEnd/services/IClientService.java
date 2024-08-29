package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.entity.Client;

import java.util.Optional;

public interface IClientService {
    Optional<Client> findClientByIdClient(Long idClient);
    Client saveClient(Client client);
}

