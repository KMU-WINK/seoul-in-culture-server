package com.github.kmu_wink.seoul_in_culture.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank
        String token

) {
}
