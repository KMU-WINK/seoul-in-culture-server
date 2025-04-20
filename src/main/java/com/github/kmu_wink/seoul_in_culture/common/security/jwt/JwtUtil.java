package com.github.kmu_wink.seoul_in_culture.common.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.kmu_wink.seoul_in_culture.common.property.JwtProperty;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperty jwtProperty;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {

        algorithm = Algorithm.HMAC256(jwtProperty.getKey());
    }

    public String generateToken(User user) {

        return generateToken(user.getId());
    }

    public String generateToken(String userId) {

        return JWT.create().withIssuedAt(Instant.now()).withClaim("id", userId).sign(algorithm);
    }

    public String extractToken(String token) {

        return JWT.require(algorithm).build().verify(token).getClaim("id").asString();
    }

    public boolean validateToken(String token) throws TokenExpiredException {

        if (Objects.isNull(token)) {

            return false;
        }

        JWT.require(algorithm).build().verify(token);

        return true;
    }
}
