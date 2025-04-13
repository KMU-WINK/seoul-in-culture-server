package com.github.kmu_wink.seoul_in_culture.domain.auth.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.request.LoginRequest;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.GetMyTokenInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.LoginResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.service.AuthService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "[인증]")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {

        return ApiResponse.ok(authService.login(request));
    }

    @AuthGuard
    @GetMapping("/me")
    @Operation(summary = "토큰으로 정보 조회")
    public ApiResponse<GetMyTokenInfoResponse> getMyTokenInfo(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.ok(authService.getMyTokenInfo(user));
    }
}
