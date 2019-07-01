package com.demo.account.accountapp.service;

import com.demo.account.accountapp.dao.entity.ClientEntity;
import com.demo.account.accountapp.error.BusinessException;
import com.demo.account.accountapp.mapper.create.ClientAndClientEntityMapper;
import com.demo.account.accountapp.model.Client;
import com.demo.account.accountapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientHandlerService {

    private final ClientAndClientEntityMapper clientAndClientEntityMapper;

    private final ClientRepository clientRepository;

    @Autowired
    public ClientHandlerService(final ClientAndClientEntityMapper mapper, final ClientRepository repo){
        this.clientAndClientEntityMapper = mapper;
        this.clientRepository = repo;

    }

    public Client createClient(final Client client) {
        final ClientEntity clientEntity = clientAndClientEntityMapper.clientToClientEntity(client);
        final ClientEntity savedClientEntity = clientRepository.save(clientEntity);
        return clientAndClientEntityMapper.clientEntityToClient(savedClientEntity);
    }

    public Set<Client> listClients() {
        final List<ClientEntity> clientEntities = clientRepository.findAll();
        return clientEntities.stream()
                .map(clientAndClientEntityMapper::clientEntityToClient)
                .collect(Collectors.toSet());
    }

    public Client getClient(final Long clientId) {
        final Optional<ClientEntity> clientEntity = clientRepository.findById(clientId);
        return clientEntity.map(clientAndClientEntityMapper::clientEntityToClient)
                .orElseThrow(() -> new BusinessException("No client exists with " + clientId));

    }

}
