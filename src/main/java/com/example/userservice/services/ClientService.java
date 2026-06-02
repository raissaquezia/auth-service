package com.example.userservice.services;

import com.example.userservice.dtos.Dtos;
import com.example.userservice.entities.Client;
import com.example.userservice.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Dtos.ClientRegistrationResponse registerClient(Dtos.ClientRegistrationRequest request) {
        String clientId = request.getClientName().toLowerCase().replaceAll("\\s+", "-") + "-" + UUID.randomUUID().toString().substring(0, 8);
        String rawSecret = UUID.randomUUID().toString();
        
        Client client = Client.builder()
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(rawSecret))
                .redirectUris(request.getRedirectUris())
                .scopes(request.getScopes())
                .grantTypes(request.getGrantTypes())
                .build();

        clientRepository.save(client);

        Dtos.ClientRegistrationResponse response = new Dtos.ClientRegistrationResponse();
        response.setClientId(clientId);
        response.setClientSecret(rawSecret);
        return response;
    }
}
