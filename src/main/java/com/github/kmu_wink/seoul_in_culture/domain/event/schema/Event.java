package com.github.kmu_wink.seoul_in_culture.domain.event.schema;

import java.time.LocalDate;

import com.github.kmu_wink.seoul_in_culture.common.database.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseSchema {

    Category category;

    String image;
    String title;

    LocalDate startDate;
    LocalDate endDate;

    String host;
    User.District district;
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
}