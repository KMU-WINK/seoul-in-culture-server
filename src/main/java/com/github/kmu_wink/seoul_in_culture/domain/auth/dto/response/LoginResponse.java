package com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse (

    String token
) {
}
