package com.github.kmu_wink.seoul_in_culture.domain.user.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.kmu_wink.seoul_in_culture.common.mongo.BaseSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class User extends BaseSchema {

    @Indexed
    @JsonIgnore
    long kakao;

    String avatar;

    @Indexed
    String nickname;

    @Indexed
    String email;

    Integer birthYear;
    District district;
    Gender gender;

    public Integer getAge() {

        if (Objects.isNull(birthYear)) {
            return null;
        }

        return LocalDate.now().getYear() - birthYear + 1;
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    @SuppressWarnings("NonAsciiCharacters")
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
        강동구,
        그외
    }
}