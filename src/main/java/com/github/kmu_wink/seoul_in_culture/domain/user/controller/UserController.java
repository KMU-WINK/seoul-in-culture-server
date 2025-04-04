package com.github.kmu_wink.seoul_in_culture.domain.user.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.MyDetailResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.OtherDetailResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.UserEditRequest;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.UserEditResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import com.github.kmu_wink.seoul_in_culture.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User-Controller", description = "유저-관련-API")
public class UserController {

    private final UserService userService;

    @PutMapping
    @Operation(summary = "프로필 수정", description = "유저의 프로필 수정합니다.")
    public ApiResponse<UserEditResponse> editUser(
            @RequestBody UserEditRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.ok(userService.editUser(user, request));
    }

    @GetMapping
    @Operation(summary = "내 상세 페이지 조회", description = "내 상세 페이지를 조회합니다.")
    public ApiResponse<MyDetailResponse> getMyDetail(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.ok(userService.getMyDetail(user));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "다른 유저 상세 페이지 조회", description = "다른 유저의 상세 페이지를 조회합니다.")
    public ApiResponse<OtherDetailResponse> getOtherDetail(
            @PathVariable String userId
    ) {
        return ApiResponse.ok(userService.getOtherDetail(userId));
    }
}
