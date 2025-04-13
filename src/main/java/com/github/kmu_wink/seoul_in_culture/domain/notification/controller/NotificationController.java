package com.github.kmu_wink.seoul_in_culture.domain.notification.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.notification.dto.request.SubscribeRequest;
import com.github.kmu_wink.seoul_in_culture.domain.notification.dto.response.GetNotificationsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.notification.service.NotificationService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AuthGuard
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "[알림]")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "알림 목록")
    public ApiResponse<GetNotificationsResponse> getNotifications(
            @AuthenticationPrincipal User user
    ) {

        return ApiResponse.ok(notificationService.getNotifications(user));
    }

    @PostMapping("/{notificationId}")
    @Operation(summary = "알림 읽기")
    public ApiResponse<Void> readNotification(
            @AuthenticationPrincipal User user,
            @PathVariable String notificationId
    ) {

        notificationService.readNotification(user, notificationId);

        return ApiResponse.ok();
    }

    @PostMapping("/all")
    @Operation(summary = "알림 모두 읽기")
    public ApiResponse<Void> readAllNotification(
            @AuthenticationPrincipal User user
    ) {

        notificationService.readAllNotification(user);

        return ApiResponse.ok();
    }

    @PostMapping("/subscribe")
    @Operation(summary = "FCM 구독")
    public ApiResponse<Void> subscribe(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid SubscribeRequest request
    ) {

        notificationService.subscribe(user, request);

        return ApiResponse.ok();
    }
}

