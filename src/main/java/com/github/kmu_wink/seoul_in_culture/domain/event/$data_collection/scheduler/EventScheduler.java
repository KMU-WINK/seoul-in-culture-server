package com.github.kmu_wink.seoul_in_culture.domain.event.$data_collection.scheduler;

import com.github.kmu_wink.seoul_in_culture.domain.event.$data_collection.api.EventApiClient;
import com.github.kmu_wink.seoul_in_culture.domain.event.$data_collection.dto.EventRequest;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventApiClient eventApiClient;
    private final MongoTemplate mongoTemplate;

    // 매일 정해진 시간마다 API 호출하여 데이터를 MongoDB에 저장
    @Scheduled(fixedRate = 86400000)  // 24시간마다 실행 (24시간 = 86400000 밀리초)
    public void fetchAndSaveEventData() {
        log.info("이벤트 데이터를 주기적으로 가져옵니다...");

        List<EventRequest> events = eventApiClient.fetchEventList();

        if (events.isEmpty()) {
            log.info("가져온 이벤트 데이터가 없습니다.");
        } else {
            for (EventRequest eventRequest : events) {
                Event event = Event.builder()
                        .title(eventRequest.getTitle())
                        .category(parseCategory(eventRequest.getCategory()))
                        .description(eventRequest.getDescription())
                        .image(eventRequest.getMainImage())
                        .startDate(parseDate(eventRequest.getStartDate()))
                        .endDate(parseDate(eventRequest.getEndDate()))
                        .host(eventRequest.getOrgName())
                        .district(parseDistrict(eventRequest.getDistrict()))
                        .location(eventRequest.getPlace())
                        .target(eventRequest.getUseTarget())
                        .free("무료".equals(eventRequest.getIsFree()))
                        .cost(eventRequest.getFeeInfo())
                        .homepage(eventRequest.getHomepageUrl())
                        .build();

                // MongoDB에 저장
//                mongoTemplate.save(event);

                saveOrUpdateEvent(event);
                log.info("이벤트 '{}' 저장 완료", event.getTitle());
            }
        }
    }

    // 날짜 파싱 메서드 (예시)
    private LocalDateTime parseDate(String date) {
        // 날짜와 시간을 포함하는 포맷 (예시: 2025-01-10 00:00:00.0)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        try {
            return LocalDateTime.parse(date, formatter);  // 정확한 날짜 형식에 맞춰서 파싱
        } catch (Exception e) {
            log.warn("날짜 변환 실패: {}", date);
            return null;  // 변환 실패 시 null 반환
        }
    }

    // 카테고리 파싱 메서드 (예시)
    private Event.Category parseCategory(String category) {
        String formattedCategory = category.replace("/", "").replace("-", "").trim();
        try {
            return Event.Category.valueOf(formattedCategory);
        } catch (IllegalArgumentException e) {
            System.out.println("잘못된 카테고리 값: " + formattedCategory);
            return null;  // 기본값 설정
        }
    }

    // 지역 파싱 메서드 (예시)
    private User.District parseDistrict(String district) {
        try {
            return User.District.valueOf(district);
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 구명 값: {}", district);
            return null;  // 기본값 설정
        }
    }

    private void saveOrUpdateEvent(Event event) {
        // 제목을 기준으로 이미 존재하는 데이터 확인 (예: title을 기준으로 중복 체크)
        Event existingEvent = mongoTemplate.findOne(Query.query(Criteria.where("title").is(event.getTitle())), Event.class);

        if (existingEvent != null) {
            // 기존 데이터가 있으면 업데이트
            log.info("기존 이벤트 '{}' 업데이트", event.getTitle());
            event.setId(existingEvent.getId());  // 기존 ID를 덮어쓰기 위해 설정
            mongoTemplate.save(event);  // 업데이트 처리
        } else {
            // 기존 데이터가 없으면 새로 저장
            log.info("새로운 이벤트 '{}' 저장", event.getTitle());
            mongoTemplate.save(event);  // 새로 저장
        }
    }
}

