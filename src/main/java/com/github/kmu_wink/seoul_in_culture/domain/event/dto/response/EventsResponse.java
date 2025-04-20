package com.github.kmu_wink.seoul_in_culture.domain.event.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;

@Builder
public record EventsResponse(

        Collection<EventDto> events
) {

    @Builder
    public record EventDto(

            @Field("_id")
            Event event,
            int meetings
    ) {

    }
}
