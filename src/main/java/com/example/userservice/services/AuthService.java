package com.example.userservice.services;

import com.example.userservice.dtos.LoginRequestDTO;
import com.example.userservice.dtos.RegisterRequestDTO;
import com.example.userservice.dtos.TokenResponseDTO;
import com.example.userservice.entities.Client;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.ClientNotFoundException;
import com.example.userservice.exceptions.ForbiddenRoleAssignmentException;
import com.example.userservice.exceptions.InvalidCredentialsException;
import com.example.userservice.exceptions.UserAlreadyExistsException;
import com.example.userservice.repositories.ClientRepository;
import com.example.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Fluxo de Login (Autenticação)
     */
    public TokenResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByLoginAndClient_Id(request.login(), request.clientId())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);
        Long expiresIn = user.getClient().getTokenExpirationMillis();

        return new TokenResponseDTO(token, expiresIn);
    }

    /**
     * Fluxo de Cadastro (Registro)
     */
    public void register(RegisterRequestDTO request) {
        if ("SUPER_ADMIN".equalsIgnoreCase(request.role())) {
            throw new ForbiddenRoleAssignmentException();
        }

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(ClientNotFoundException::new);

        if (userRepository.findByLoginAndClient_Id(request.login(), request.clientId()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User newUser = new User();
        newUser.setLogin(request.login());
        newUser.setRole(request.role());
        newUser.setClient(client);

        newUser.setPasswordHash(passwordEncoder.encode(request.password()));

        userRepository.save(newUser);
    }
}
