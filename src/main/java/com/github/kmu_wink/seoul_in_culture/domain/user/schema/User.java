package com.github.kmu_wink.seoul_in_culture.domain.user.schema;

import com.github.kmu_wink.seoul_in_culture.common.database.mongo.BaseSchema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseSchema {

    long kakao;

    String avatar;
    String nickname;
    String email;

    int experience;

    int birthYear;
    String district;
    Gender gender;

    boolean meetingOpen;

    public enum Gender {
        MALE, FEMALE
    }
}