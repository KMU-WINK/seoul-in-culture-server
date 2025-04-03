package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import lombok.Builder;

@Builder
public record ReviewDto(
        String id,
        AuthorDto author,
        int score,
        String content
) {
}
