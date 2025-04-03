package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

public record UserEditRequest(
        String avatar,
        String nickname,
        User.District district,
        boolean meetingOpen
) {}