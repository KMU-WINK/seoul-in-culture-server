package com.github.kmu_wink.seoul_in_culture.domain.event.schema;

import java.time.LocalDateTime;

import com.github.kmu_wink.seoul_in_culture.common.database.mongo.BaseSchema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseSchema {

    Category category;

    String image;
    String title;

    LocalDateTime startDate;
    LocalDateTime endDate;

    String host;
    District district;
    String location;
    String target;
    boolean free;
    String cost;
    String homepage;

    public enum Category {

        교육체험,
        국악,
        기타,
        독주독창회,
        무용,
        뮤지컬오페라,
        연극,
        영화,
        전시미술,
        축제기타,
        축제문화예술,
        축제시민화합,
        축제자연경관,
        축제전통역사,
        콘서트,
        클래식
    }

    public enum District {
        종로구,
        중구,
        용산구,
        성동구,
        광진구,
        동대문구,
        중랑구,
        성북구,
        강북구,
        도봉구,
        노원구,
        은평구,
        서대문구,
        마포구,
        양천구,
        강서구,
        구로구,
        금천구,
        영등포구,
        동작구,
        관악구,
        서초구,
        강남구,
        송파구,
        강동구
    }
}