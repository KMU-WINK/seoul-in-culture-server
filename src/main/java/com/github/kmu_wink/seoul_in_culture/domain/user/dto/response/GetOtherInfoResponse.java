package com.github.kmu_wink.seoul_in_culture.domain.user.dto.response;

import java.util.Collection;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.Builder;

@Builder
public record GetOtherInfoResponse(

        User user,

        int bookmark,

        int joinedMeeting,
		Collection<Meeting> hostedMeeting,

		Collection<MeetingReview> review
) {
}
