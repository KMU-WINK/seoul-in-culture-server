package com.github.kmu_wink.yeogichadae2.domain.user.entity;

import com.github.kmu_wink.yeogichadae2.common.BaseSchema;
import jakarta.persistence.Entity;
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

    String name;
    String nickname;
    String avatar;
    String phoneNumber;
    String district;
    Gender gender;
    int birthYear;
    float mannerScore;

    public enum Gender {
        MALE, FEMALE
    }
}
