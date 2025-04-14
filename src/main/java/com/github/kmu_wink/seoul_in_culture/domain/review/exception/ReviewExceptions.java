package com.github.kmu_wink.seoul_in_culture.domain.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewExceptions {

    ALREADY_REVIEW("이미 리뷰를 작성한 유저입니다."),
    ;

    private final String message;
}
