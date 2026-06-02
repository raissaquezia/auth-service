package com.example.userservice.services;

import com.example.userservice.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final String clientId = resolveClientId();

        System.out.println("LOGIN: " + login);
        System.out.println("CLIENT ID RESOLVIDO: " + clientId);

        return userRepository.findByLoginAndClientId(login, clientId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + login + " para o cliente: " + clientId
                ));
    }

    private String resolveClientId() {
        // 1. Tenta do Header (API calls)
        String clientId = request.getHeader("X-Client-Id");

        // 2. Tenta do Parâmetro (Direct calls)
        if (clientId == null || clientId.isEmpty()) {
            clientId = request.getParameter("client_id");
        }

        // 3. Tenta da Sessão (Fluxo de Login via Browser)
        if (clientId == null || clientId.isEmpty()) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                clientId = (String) session.getAttribute("OAUTH2_CLIENT_ID");
            }
        }

        if (clientId == null || clientId.isEmpty()) {
            throw new UsernameNotFoundException(
                    "A requisição deve conter o Client ID no header 'X-Client-Id', parâmetro 'client_id' ou na sessão"
            );
        }

        return clientId;
    }
}
