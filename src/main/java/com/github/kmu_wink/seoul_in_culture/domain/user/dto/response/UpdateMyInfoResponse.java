package com.github.kmu_wink.seoul_in_culture.domain.user.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;

@Builder
public record UpdateMyInfoResponse(

        User user
) {
}