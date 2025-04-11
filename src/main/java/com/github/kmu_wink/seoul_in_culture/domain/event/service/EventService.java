package com.github.kmu_wink.seoul_in_culture.domain.event.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.dto.EventReponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Locale.filter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getEvents(String date, String category, String district, String price) {
        LocalDate filterDate;
        if(date!=null) filterDate = LocalDate.parse(date);
        else filterDate = null;

//        boolean isFree = "free".equalsIgnoreCase(price);
        Boolean isFree;
        if ("free".equalsIgnoreCase(price)) {
            isFree = true;  // 무료인 이벤트만 가져오려는 경우
        } else if ("paid".equalsIgnoreCase(price)) {
            isFree = false; // 유료인 이벤트만 가져오려는 경우
        } else {
            isFree = null;
        }

        return eventRepository.findAll().stream()
                .filter(event -> isEventMatching(event, filterDate, category, district, isFree))
                .collect(Collectors.toList());
    }

    private boolean isEventMatching(Event event, LocalDate date, String category, String district, Boolean isFree) {

        // 날짜 필터링 (날짜가 null일 경우 필터링을 하지 않음)
        boolean isWithinDateRange = true;
        if (date != null) {
            ChronoLocalDateTime<LocalDate> chronoLocalDate = date.atStartOfDay();
            isWithinDateRange = (event.getStartDate().isEqual(chronoLocalDate) || event.getStartDate().isBefore(chronoLocalDate)) &&
                    (event.getEndDate().isEqual(chronoLocalDate) || event.getEndDate().isAfter(chronoLocalDate));
        }

        // 카테고리 필터링
        boolean isCategoryMatching = category == null || event.getCategory().name().equalsIgnoreCase(category);

        // 지역구 필터링
        boolean isDistrictMatching = (district == null || event.getDistrict() == null || district.equals(event.getDistrict().name()));

        // 무료 여부 필터링
        boolean isPriceMatching = isFree == null || event.isFree() == isFree;

        return isWithinDateRange && isCategoryMatching && isDistrictMatching && isPriceMatching;
    }
}
