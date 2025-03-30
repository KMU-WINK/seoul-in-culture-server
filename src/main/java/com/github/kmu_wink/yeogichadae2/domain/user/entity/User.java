package com.github.kmu_wink.yeogichadae2.domain.user.entity;

import com.github.kmu_wink.yeogichadae2.common.BaseSchema;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class User extends BaseSchema {

    @NotNull
    long kakao;

    @NotNull
    String nickname;

    @Nullable
    String avatar;

    @Nullable
    String district;

    @Nullable
    Gender gender;

    @Nullable
    Integer birthYear;

    float mannerScore;

    public enum Gender {
        MALE, FEMALE
    }
}
