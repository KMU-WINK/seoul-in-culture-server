package com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.request;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateMeetingRequest(

        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotNull
        LocalDateTime datetime,

        @Min(2)
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
