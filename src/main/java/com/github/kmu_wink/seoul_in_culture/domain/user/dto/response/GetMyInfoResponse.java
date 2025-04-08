package com.github.kmu_wink.seoul_in_culture.domain.user.dto.response;

import java.util.Collection;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.Review;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.Builder;

@Builder
public record GetMyInfoResponse(

        User user,

		Collection<Bookmark> bookmark,

        int joinedMeeting,
        int hostedMeeting,

		Collection<Review> review
) {
}

