package com.healthaiharbor.ai.userservice.repository;

import com.healthaiharbor.ai.userservice.modal.RefreshToken;
import com.healthaiharbor.ai.userservice.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByToken(String token);

    Optional<RefreshToken> findByUser(User user);
}
