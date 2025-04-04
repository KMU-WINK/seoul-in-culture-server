package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserEditRequest(
        @Schema(description = "프로필 이미지")
        String avatar,

        @Schema(description = "닉네임")

        String nickname,

        @Schema(description = "지역구")

        User.District district,

        @Schema(description = "참여 중인 모임 공개 여부")

        boolean meetingOpen
) {}