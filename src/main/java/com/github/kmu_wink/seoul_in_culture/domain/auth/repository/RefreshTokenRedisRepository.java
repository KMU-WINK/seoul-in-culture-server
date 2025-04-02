package com.github.kmu_wink.seoul_in_culture.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

import com.github.kmu_wink.seoul_in_culture.domain.auth.schema.RefreshToken;

public interface RefreshTokenRedisRepository extends KeyValueRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
}
