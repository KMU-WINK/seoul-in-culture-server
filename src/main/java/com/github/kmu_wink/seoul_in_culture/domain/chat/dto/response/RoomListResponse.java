package com.github.kmu_wink.seoul_in_culture.domain.chat.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import lombok.Builder;

import java.util.Collection;

@Builder
public record RoomListResponse(

        Collection<Room> rooms
) {

    @Builder
    public record Room(

            Meeting meeting,
            Chat last
    ) {

    }
}
