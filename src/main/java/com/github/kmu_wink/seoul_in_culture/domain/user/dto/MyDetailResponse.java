package com.github.kmu_wink.seoul_in_culture.domain.user.dto;


import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;

import java.util.List;

@Builder
public record MyDetailResponse(
        User user,
        List<Bookmark> bookmark,
        int joinedMeeting,
        int hostedMeeting,
        List<MeetingReview> review
) {
}

