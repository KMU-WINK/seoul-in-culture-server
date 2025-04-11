package com.github.kmu_wink.seoul_in_culture.domain.event.$data_collection.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kmu_wink.seoul_in_culture.domain.event.$data_collection.dto.EventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventApiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    private final String API_URL = "http://openapi.seoul.go.kr:8088/7767424e62736f6e35307474784456/json/culturalEventInfo/1/1000/";

    public List<EventRequest> fetchEventList() {
        try {
            String response = restTemplate.getForObject(API_URL, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode rows = root.path("culturalEventInfo").path("row");

            List<EventRequest> result = new ArrayList<>();
            for (JsonNode row : rows) {
                EventRequest eventRequest = objectMapper.treeToValue(row, EventRequest.class);
                // startDate와 endDate 로그로 확인
                log.info("시작!! Date: {}", eventRequest.getStartDate());
                log.info("끝!! Date: {}", eventRequest.getEndDate());
                result.add(eventRequest);
            }

            log.info("총 {}개의 이벤트 데이터를 불러왔습니다.", result.size());
            return result;

        } catch (Exception e) {
            log.error("API 호출 또는 파싱 중 오류 발생", e);
            return new ArrayList<>();
        }
    }
}