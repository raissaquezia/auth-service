package com.example.userservice.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.userservice.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);

        if (token != null) {
            System.out.println("\n--- DEBUG DE SEGURANÇA ---");
            DecodedJWT decodedJWT = jwtService.validateToken(token);

            if (decodedJWT != null) {
                String login = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();

                System.out.println("✅ Token Válido! Role lida do token: " + role);

                var authority = new SimpleGrantedAuthority("ROLE_" + role);
                System.out.println("🛡️ Autoridade injetada: " + authority.getAuthority());

                var authentication = new UsernamePasswordAuthenticationToken(login, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("❌ ERRO: O Token falhou na validação matemática (secret errado ou expirado)!");
            }
            System.out.println("--------------------------\n");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}