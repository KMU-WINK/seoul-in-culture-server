package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response;

import java.util.Collection;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;

import lombok.Builder;

@Builder
public record GetMyMeetingsResponse(

    Collection<Meeting> meetings
) {
}