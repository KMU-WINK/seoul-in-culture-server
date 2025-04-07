package com.github.kmu_wink.seoul_in_culture.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.request.UserEditRequest;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.GetMyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.GetOtherInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.UpdateMyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import com.github.kmu_wink.seoul_in_culture.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "[유저]")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "내 상세 페이지 조회")
    public ApiResponse<GetMyInfoResponse> getMyDetailInfo(
        @AuthenticationPrincipal User user
    ) {

        return ApiResponse.ok(userService.getMyInfo(user));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "다른 유저 상세 페이지 조회")
    public ApiResponse<GetOtherInfoResponse> getOtherDetailInfo(
        @PathVariable String userId
    ) {

        return ApiResponse.ok(userService.getOtherInfo(userId));
    }

    @PutMapping
    @Operation(summary = "프로필 수정")
    public ApiResponse<UpdateMyInfoResponse> updateMyInfo(
        @AuthenticationPrincipal User user,
        @RequestBody @Valid UserEditRequest request
    ) {

        return ApiResponse.ok(userService.updateMyInfo(user, request));
    }
}
