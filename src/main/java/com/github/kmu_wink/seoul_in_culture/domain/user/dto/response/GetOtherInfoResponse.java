package com.github.kmu_wink.seoul_in_culture.domain.user.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;

import java.util.Collection;

@Builder
public record GetOtherInfoResponse(

        User user,

        long bookmarks,

        long joinedMeetings,
        Collection<Meeting> hostedMeetings,

        Collection<Review> reviews,

        double score
) {

}
