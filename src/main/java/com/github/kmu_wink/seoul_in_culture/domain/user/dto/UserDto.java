package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDto(
        String id,
        String nickname,
        LocalDateTime createdAt,
        String email,
        String district,
        double score,
        int experience,
        String gender,
        int age
) {
}
