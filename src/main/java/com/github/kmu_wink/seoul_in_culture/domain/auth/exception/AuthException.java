package com.github.kmu_wink.seoul_in_culture.domain.auth.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class AuthException extends ApiException {

    private AuthException(AuthExceptions authExceptions) {

        super(authExceptions.getMessage());
    }

    public static AuthException of(AuthExceptions authExceptions) {

        return new AuthException(authExceptions);
    }
}
