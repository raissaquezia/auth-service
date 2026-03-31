package com.example.userservice.services;

import com.example.userservice.dtos.ClientRequestDTO;
import com.example.userservice.dtos.ClientResponseDTO;
import com.example.userservice.entities.Client;
import com.example.userservice.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientResponseDTO createClient(ClientRequestDTO request) {
        Client client = new Client();
        client.setName(request.name());
        client.setTokenExpirationMillis(request.tokenExpirationMillis());

        // Salva no banco de dados e gera o UUID
        Client savedClient = clientRepository.save(client);

        return new ClientResponseDTO(
                savedClient.getId(),
                savedClient.getName(),
                savedClient.getTokenExpirationMillis()
        );
    }

    public List<ClientResponseDTO> listAll() {
        return clientRepository.findAll().stream()
                .map(c -> new ClientResponseDTO(c.getId(), c.getName(), c.getTokenExpirationMillis()))
                .toList();
    }
}