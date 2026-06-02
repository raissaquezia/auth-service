package com.example.userservice.services;

import com.example.userservice.entities.Session;
import com.example.userservice.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public Session createSession(UUID userId, String device, String ip, String refreshToken, long expiresInSeconds) {
        Session session = Session.builder()
                .userId(userId)
                .device(device)
                .ip(ip)
                .refreshToken(refreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(expiresInSeconds))
                .active(true)
                .build();
        return sessionRepository.save(session);
    }

    public Optional<Session> findByRefreshToken(String refreshToken) {
        return sessionRepository.findByRefreshToken(refreshToken)
                .filter(Session::isActive)
                .filter(s -> s.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Transactional
    public void invalidateSession(UUID sessionId) {
        sessionRepository.findById(sessionId).ifPresent(session -> {
            session.setActive(false);
            sessionRepository.save(session);
        });
    }

    @Transactional
    public void invalidateByRefreshToken(String refreshToken) {
        sessionRepository.findByRefreshToken(refreshToken).ifPresent(session -> {
            session.setActive(false);
            sessionRepository.save(session);
        });
    }

    @Transactional
    public void invalidateAllUserSessions(UUID userId) {
        List<Session> sessions = sessionRepository.findByUserIdAndActiveTrue(userId);
        sessions.forEach(session -> session.setActive(false));
        sessionRepository.saveAll(sessions);
    }

    public List<Session> getUserSessions(UUID userId) {
        return sessionRepository.findByUserIdAndActiveTrue(userId);
    }

    @Transactional
    public void updateLastAccess(Session session) {
        session.setLastAccess(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
