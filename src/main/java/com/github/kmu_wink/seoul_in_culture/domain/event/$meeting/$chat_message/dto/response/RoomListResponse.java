package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response;

import java.util.Collection;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema.ChatMessage;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;

import lombok.Builder;

@Builder
public record RoomListResponse(

	Collection<Room> rooms
) {

	@Builder
	public record Room(

		Meeting meeting,
		ChatMessage last
	) {
	}
}
