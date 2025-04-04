package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OtherDetailResponse(
        UserDto user,
        int bookmark,
        int joinedMeeting,
        List<ReviewDto> review,
        List<MeetingDto> hostedMeeting
) {}
