package com.github.kmu_wink.seoul_in_culture.domain.user.dto.request;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserEditRequest(

        @Nullable
        String avatar,

        @NotBlank
        String nickname,

        @NotNull
        User.District district,

        boolean meetingOpen
) {
}