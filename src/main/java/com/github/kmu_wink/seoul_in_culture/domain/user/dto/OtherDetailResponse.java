package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;

import java.util.List;

@Builder
public record OtherDetailResponse(
        User user,
        int bookmark,
        int joinedMeeting,
        List<MeetingReview> review,

        List<Meeting> hostedMeeting
) {}
