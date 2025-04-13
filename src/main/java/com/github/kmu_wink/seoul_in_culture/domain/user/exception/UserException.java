package com.github.kmu_wink.seoul_in_culture.domain.user.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class UserException extends ApiException {

    private UserException(UserExceptions userExceptions) {

        super(userExceptions.getMessage());
    }

    public static UserException of(UserExceptions userExceptions) {

        return new UserException(userExceptions);
    }
}
