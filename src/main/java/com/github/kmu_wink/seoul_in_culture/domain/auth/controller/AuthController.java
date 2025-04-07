package com.github.kmu_wink.seoul_in_culture.domain.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.request.LoginRequest;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.LoginResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.GetMyTokenInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.RefreshTokenResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.service.AuthService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@Tag(name = "[인증]")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
        @RequestBody @Valid LoginRequest request
    ) {

        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ApiResponse<RefreshTokenResponse> refreshToken(
        @RequestBody @Valid LoginRequest request
    ) {

        return ApiResponse.ok(authService.refreshToken(request));
    }

    @AuthGuard
    @GetMapping("/me")
    public ApiResponse<GetMyTokenInfoResponse> getMyTokenInfo(
        @AuthenticationPrincipal User user
    ) {
        return ApiResponse.ok(authService.getMyTokenInfo(user));
    }
}
