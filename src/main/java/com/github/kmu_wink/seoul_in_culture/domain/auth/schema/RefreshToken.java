package com.github.kmu_wink.seoul_in_culture.domain.auth.schema;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;

@Builder
@RedisHash(value = "refresh_token")
public record RefreshToken (

    @Id
    Long id,

    @Indexed
    String token,

    String userId,

    @TimeToLive
    long ttl
) {
}
