package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import lombok.Builder;

@Builder
public record AuthorDto(
        String createdAt,
        String avatar,
        String nickname
) {
}
