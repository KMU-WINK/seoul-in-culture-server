package com.github.kmu_wink.seoul_in_culture.domain.auth.dto;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;

@Builder
public record MyInfoResponse(
        User user
) {
}
