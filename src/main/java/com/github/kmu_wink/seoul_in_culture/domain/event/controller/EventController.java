package com.github.kmu_wink.seoul_in_culture.domain.event.controller;

import com.github.kmu_wink.seoul_in_culture.domain.event.dto.EventReponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/event/calendar")
    public EventReponse getEventsByRequirements(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "price", required = false) String price
    ) {
        return new EventReponse(eventService.getEvents(date,category,district,price));
    }
}
