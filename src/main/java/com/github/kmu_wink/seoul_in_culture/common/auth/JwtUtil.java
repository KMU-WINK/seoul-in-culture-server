package com.github.kmu_wink.seoul_in_culture.common.auth;

import java.time.Instant;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.kmu_wink.seoul_in_culture.common.property.JwtProperty;
import com.github.kmu_wink.seoul_in_culture.domain.auth.schema.RefreshToken;
import com.github.kmu_wink.seoul_in_culture.domain.auth.repository.RefreshTokenRedisRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperty jwtProperty;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {

        algorithm = Algorithm.HMAC256(jwtProperty.getKey());
    }

    public String generateAccessToken(User user) {

        return generateAccessToken(user.getId());
    }

    public String generateAccessToken(String userId) {

        return JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(jwtProperty.getAccessTokenExpirationHours() * 3600L))
                .withClaim("id", userId)
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        return generateRefreshToken(user.getId());
    }

    public String generateRefreshToken(String userId) {
        String token = JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(jwtProperty.getRefreshTokenExpirationHours() * 3600L))
                .sign(algorithm);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .ttl(jwtProperty.getRefreshTokenExpirationHours()* 3600L)
                .build();

        refreshTokenRedisRepository.save(refreshToken);

        return token;
    }

    public String extractToken(String token) {

        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getClaim("id")
                .asString();
    }

    public boolean validateToken(String token) throws TokenExpiredException {

        if (Objects.isNull(token)) {

            return false;
        }

        JWT.require(algorithm).build().verify(token);

        return true;
    }
}
