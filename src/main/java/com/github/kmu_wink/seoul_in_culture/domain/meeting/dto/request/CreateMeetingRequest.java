package com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.request;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.github.kmu_wink.seoul_in_culture.common.validation.RegExp.YYYY_MM_DD_HH_MM_EXPRESSION;
import static com.github.kmu_wink.seoul_in_culture.common.validation.RegExp.YYYY_MM_DD_HH_MM_MESSAGE;

public record CreateMeetingRequest(

        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotBlank
        @Pattern(regexp = YYYY_MM_DD_HH_MM_EXPRESSION, message = YYYY_MM_DD_HH_MM_MESSAGE)
        String datetime,

        @Min(1)
        int maxPeople,

        @Min(15)
        @Max(70)
        @Nullable
        Integer minAge,

        @Min(15)
        @Max(70)
        @Nullable
        Integer maxAge,

        @Nullable
        User.Gender gender
) {
}
