package com.github.kmu_wink.seoul_in_culture.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptions {

	FAIL_AUTHENTICATION("인증에 실패하였습니다."),
	EXPIRED_ACCESS_TOKEN("액세스 토큰이 만료되었습니다."),
	INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다."),
	INVALID_KAKAO_TOKEN("유효하지 않은 카카오 인증 토큰입니다."),
	;

	private final String message;
}
