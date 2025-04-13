package com.github.kmu_wink.seoul_in_culture.domain.auth.dto.internal;

import lombok.Builder;

@Builder
public record KakaoUser(

        long id,
        String email
) {
}
