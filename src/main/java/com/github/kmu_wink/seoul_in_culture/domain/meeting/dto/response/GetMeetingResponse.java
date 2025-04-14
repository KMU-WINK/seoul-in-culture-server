package com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import lombok.Builder;

@Builder
public record GetMeetingResponse(

        Meeting meeting
) {
}
