package com.github.kmu_wink.seoul_in_culture.domain.event.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import lombok.Builder;

@Builder
public record EventResponse(

        Event event
) {

}
