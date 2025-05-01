package com.github.kmu_wink.seoul_in_culture.domain.event.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.dto.response.EventResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.dto.response.EventsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.event.service.EventService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "[행사]")
public class EventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "행사 목록")
    public ApiResponse<EventsResponse> getEvents(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) List<Event.Category> categories,
            @RequestParam(required = false) List<User.District> districts,
            @RequestParam(required = false) Boolean isFree
    ) {

        return ApiResponse.ok(eventService.getEvents(date, searchQuery, categories, districts, isFree));
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "행사 목록")
    public ApiResponse<EventResponse> getEvent(@PathVariable String eventId) {

        return ApiResponse.ok(eventService.getEvent(eventId));
    }

    @GetMapping("/advertised")
    @Operation(summary = "광고 중인 행사 목록")
    public ApiResponse<EventsResponse> getAdvertisedEvent() {

        return ApiResponse.ok(eventService.getAdvertisedEvent());
    }
}