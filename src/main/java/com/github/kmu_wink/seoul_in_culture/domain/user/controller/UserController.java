package com.github.kmu_wink.seoul_in_culture.domain.user.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.UserEditRequest;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.UserEditResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import com.github.kmu_wink.seoul_in_culture.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping
    public ApiResponse<UserEditResponse> editUser(
            @RequestBody UserEditRequest request,
            @AuthenticationPrincipal User user
            ) {
        return ApiResponse.ok(userService.editUser(user.getId(), request));
    }
}
