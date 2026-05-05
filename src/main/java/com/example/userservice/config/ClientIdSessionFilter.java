package com.example.userservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ClientIdSessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestedClientId = request.getParameter("client_id");

        // Verifica se o parâmetro client_id foi informado na requisição
        if (requestedClientId != null && !requestedClientId.isEmpty()) {

            // Obtém a sessão atual sem criar uma nova, caso não exista
            HttpSession session = request.getSession(false);

            if (session != null) {
                String existingClientId = (String) session.getAttribute("OAUTH2_CLIENT_ID");

                /*
                 * Caso já exista um client_id associado à sessão e ele seja diferente
                 * do informado na requisição atual, considera-se uma troca de contexto.
                 * Nesse cenário, a sessão é invalidada para evitar inconsistências
                 * de autenticação entre diferentes clientes OAuth2.
                 */
                if (existingClientId != null && !existingClientId.equals(requestedClientId)) {
                    session.invalidate(); // Invalida a sessão atual
                    SecurityContextHolder.clearContext(); // Remove o contexto de segurança
                    session = request.getSession(true); // Cria uma nova sessão limpa
                }
            } else {
                // Cria uma nova sessão caso não exista
                session = request.getSession(true);
            }

            // Armazena o client_id atual na sessão
            session.setAttribute("OAUTH2_CLIENT_ID", requestedClientId);
        }

        filterChain.doFilter(request, response);
    }
}