package com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;

@Builder
public record GetMyTokenInfoResponse(

        User user
) {
}
