package com.github.kmu_wink.seoul_in_culture.domain.event.dto;


import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import lombok.Builder;

import java.util.List;


@Builder
public record EventReponse(List<Event> events) {
}