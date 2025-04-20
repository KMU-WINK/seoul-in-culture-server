package com.github.kmu_wink.seoul_in_culture.domain.user.dto.request;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserEditRequest(

        @NotBlank
        String nickname,

        @NotNull
        User.District district,

        @NotNull
        User.Gender gender,

        @Min(15)
        @Max(70)
        int age
) {

}