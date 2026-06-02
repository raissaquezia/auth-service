package com.example.userservice.repositories;

import com.example.userservice.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findByRefreshToken(String refreshToken);
    List<Session> findByUserIdAndActiveTrue(UUID userId);
    void deleteByUserId(UUID userId);
}
