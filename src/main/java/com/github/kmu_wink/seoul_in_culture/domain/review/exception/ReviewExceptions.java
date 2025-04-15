package com.github.kmu_wink.seoul_in_culture.domain.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewExceptions {

    ALREADY_REVIEW("이미 리뷰를 작성한 유저입니다."),
    TARGET_NOT_PARTICIPANT_MEETING("타겟 유저가 모임에 참가중이지 않습니다."),
    NOT_REVIEW_MYSELF("자기 자신은 리뷰할 수 없습니다.")
    ;

    private final String message;
}
