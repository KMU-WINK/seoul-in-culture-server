package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuthorDto(
        LocalDateTime createdAt,
        String avatar,
        String nickname
) {
}
