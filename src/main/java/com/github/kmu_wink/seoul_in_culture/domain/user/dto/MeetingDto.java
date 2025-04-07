package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;
import lombok.Builder;


@Builder
public record MeetingDto(
        MeetingParticipant meetingParticipant
) {}
