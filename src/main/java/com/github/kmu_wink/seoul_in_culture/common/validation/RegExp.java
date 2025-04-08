package com.github.kmu_wink.seoul_in_culture.common.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegExp {

	public static final String YYYY_MM_DD_HH_MM_EXPRESSION = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";
	public static final String YYYY_MM_DD_HH_MM_MESSAGE = "올바른 날짜와 시간이 아닙니다";
}
