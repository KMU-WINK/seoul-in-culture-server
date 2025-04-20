package com.github.kmu_wink.seoul_in_culture.domain.event.util.scheduler;


import com.github.kmu_wink.seoul_in_culture.common.property.SeoulDataProperty;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FetchEventListBySeoulDataApiScheduler {

    private final EventRepository eventRepository;

    private final SeoulDataProperty seoulDataProperty;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 * * * *")
    public void fetch() {

        Set<Integer> existingData = eventRepository.findAll()
                .stream()
                .map(Event::getDataId)
                .collect(Collectors.toUnmodifiableSet());

        int totalPage = getTotalPage();

        List<Event> saved = eventRepository.saveAll(IntStream.iterate(1, i -> i <= totalPage, i -> i + 1000)
                .mapToObj(i -> fetchPage(i, totalPage))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(this::flattenPage)
                .map(object -> transferEvent(object, existingData))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());

        if (saved.isEmpty()) {
            return;
        }
        log.info("{}개의 이벤트가 추가되었습니다.", saved.size());
    }

    private String generateUrl(int start, int end) {

        return "http://openapi.seoul.go.kr:8088/%s/json/culturalEventInfo/%d/%d/".formatted(
                seoulDataProperty.getKey(),
                start,
                end
        );
    }

    private int getTotalPage() {

        try (UnirestInstance instance = Unirest.spawnInstance()) {

            return instance.get(generateUrl(1, 1))
                    .asJson()
                    .getBody()
                    .getObject()
                    .getJSONObject("culturalEventInfo")
                    .getInt("list_total_count");
        }
    }

    private Optional<JSONArray> fetchPage(int start, int end) {

        try (UnirestInstance instance = Unirest.spawnInstance()) {

            return Optional.ofNullable(instance.get(generateUrl(start, Math.min(start + 999, end)))
                    .asJson()
                    .getBody()
                    .getObject()
                    .getJSONObject("culturalEventInfo")
                    .getJSONArray("row"));
        } catch (Exception e) {

            log.error("", e);
            return Optional.empty();
        }
    }

    private Stream<JSONObject> flattenPage(JSONArray data) {

        List<JSONObject> objects = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {

            objects.add(data.getJSONObject(i));
        }

        return objects.stream();
    }

    private Optional<Event> transferEvent(JSONObject object, Set<Integer> existingData) {

        return extractDataId(object).filter(dataId -> !existingData.contains(dataId))
                .map(dataId -> Event.builder()
                        .dataId(dataId)
                        .category(Event.Category.valueOf(object.getString("CODENAME").replaceAll("[/-]", "")))
                        .image(object.getString("MAIN_IMG"))
                        .title(object.getString("TITLE"))
                        .startDate(LocalDate.parse(object.getString("STRTDATE").split(" ")[0]))
                        .endDate(LocalDate.parse(object.getString("END_DATE").split(" ")[0]))
                        .applicationDate(LocalDate.parse(object.getString("RGSTDATE")))
                        .host(object.getString("ORG_NAME"))
                        .district(object.getString("GUNAME").isBlank()
                                ? null
                                : User.District.valueOf(object.getString("GUNAME")))
                        .location(object.getString("PLACE"))
                        .latitude(Double.parseDouble(object.getString("LOT")))
                        .longitude(Double.parseDouble(object.getString("LAT")))
                        .target(object.getString("USE_TRGT"))
                        .homepage(object.getString("ORG_LINK"))
                        .free(object.getString("IS_FREE").equals("무료"))
                        .cost(object.getString("USE_FEE").isBlank() ? null : object.getString("USE_FEE"))
                        .cast(object.getString("PLAYER").isBlank() ? null : object.getString("PLAYER"))
                        .description(object.getString("PROGRAM").isBlank() ? null : object.getString("PROGRAM"))
                        .other(object.getString("ETC_DESC").isBlank() ? null : object.getString("ETC_DESC"))
                        .build());
    }

    private Optional<Integer> extractDataId(JSONObject object) {

        if (!object.has("HMPG_ADDR")) {
            return Optional.empty();
        }

        Matcher matcher = Pattern.compile("cultcode=(\\d+)").matcher(object.getString("HMPG_ADDR"));

        if (!matcher.find()) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(matcher.group(1)));
    }
}
