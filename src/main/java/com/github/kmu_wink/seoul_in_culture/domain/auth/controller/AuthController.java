package com.github.kmu_wink.seoul_in_culture.domain.auth.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.auth.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.LoginRequest;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.LoginResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.MyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.service.AuthService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.ok(authService.refreshToken(request));
    }

    @GetMapping("/me")
    @AuthGuard
    public ApiResponse<MyInfoResponse> myInfo(@AuthenticationPrincipal User user) {
        return ApiResponse.ok(authService.myInfo(user));
    }
}
