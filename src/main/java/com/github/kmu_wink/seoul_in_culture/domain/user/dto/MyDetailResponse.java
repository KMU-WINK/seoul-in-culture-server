package com.github.kmu_wink.seoul_in_culture.domain.user.dto;


import lombok.Builder;

import java.util.List;

@Builder
public record MyDetailResponse(
        UserDto user,
        List<BookmarkDto> bookmark,
        int joined_meeting,
        int hosted_meeting,
        List<ReviewDto> review
) {
}

