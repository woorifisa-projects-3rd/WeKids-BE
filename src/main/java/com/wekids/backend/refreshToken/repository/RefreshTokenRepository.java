package com.wekids.backend.refreshToken.repository;

import com.wekids.backend.refreshToken.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsByToken(String token);

    @Transactional
    void deleteByToken(String token);
}
