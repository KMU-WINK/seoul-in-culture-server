package com.github.kmu_wink.seoul_in_culture.domain.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SubscribeRequest(

	@NotBlank
	String token
) {
}
