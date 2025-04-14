package com.github.kmu_wink.seoul_in_culture.domain.user.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;

import java.util.Collection;

@Builder
public record GetMyInfoResponse(

        User user,

        Collection<Bookmark> bookmark,

        int joinedMeeting,
        int hostedMeeting,

        Collection<Review> review
) {
}

