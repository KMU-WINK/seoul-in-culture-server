package com.github.kmu_wink.seoul_in_culture.domain.event.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.kmu_wink.seoul_in_culture.common.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class Event extends BaseSchema {

    @JsonIgnore
    @Indexed(unique = true)
    int dataId;

    @Indexed
    @Nonnull
    Category category;

    @Nonnull
    String image;

    @Nonnull
    String title;

    @Indexed
    @Nonnull
    LocalDate startDate;

    @Indexed
    @Nonnull
    LocalDate endDate;

    @Nonnull
    LocalDate applicationDate;

    @Nonnull
    String host;

    @Indexed
    @Nullable
    User.District district;

    @Nonnull
    String location;

    double latitude;
    double longitude;

    @Nonnull
    String target;

    @Nonnull
    String homepage;

    boolean free;

    @Nullable
    String cost;

    @Nullable
    String cast;

    @Nullable
    String description;

    @Nullable
    String other;

    @SuppressWarnings("NonAsciiCharacters")
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