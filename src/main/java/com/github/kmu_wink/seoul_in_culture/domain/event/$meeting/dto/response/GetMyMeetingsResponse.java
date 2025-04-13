package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import lombok.Builder;

import java.util.Collection;

@Builder
public record GetMyMeetingsResponse(

        Collection<Meeting> meetings
) {
}