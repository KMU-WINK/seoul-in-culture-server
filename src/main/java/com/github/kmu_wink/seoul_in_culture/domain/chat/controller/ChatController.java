package com.github.kmu_wink.seoul_in_culture.domain.chat.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.chat.dto.request.SendChatRequest;
import com.github.kmu_wink.seoul_in_culture.domain.chat.dto.response.ChatInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.chat.dto.response.RoomListResponse;
import com.github.kmu_wink.seoul_in_culture.domain.chat.dto.response.SendChatResponse;
import com.github.kmu_wink.seoul_in_culture.domain.chat.service.ChatService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@AuthGuard
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Tag(name = "[채팅]")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    @Operation(summary = "채팅방 목록")
    public ApiResponse<RoomListResponse> getRoomList(
            @AuthenticationPrincipal User user
    ) {

        return ApiResponse.ok(chatService.getRoomList(user));
    }

    @GetMapping("/{meetingId}")
    @Operation(summary = "채팅방 정보 보기 (채팅 기록 및 참가자)")
    public ApiResponse<ChatInfoResponse> getChatInfo(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId
    ) {

        return ApiResponse.ok(chatService.getChatInfo(user, meetingId));
    }

    @PostMapping("/{meetingId}")
    @Operation(summary = "채팅 보내기")
    public ApiResponse<SendChatResponse> sendChat(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId,
            @RequestBody @Valid SendChatRequest request
    ) {

        return ApiResponse.ok(chatService.sendChat(user, meetingId, request));
    }

    @PostMapping("/{meetingId}/read/all")
    @Operation(summary = "채팅 모두 읽음")
    public ApiResponse<Void> readAllChat(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId
    ) {

        chatService.readAllChat(user, meetingId);

        return ApiResponse.ok();
    }

    @PostMapping("/{chattingId}/read")
    @Operation(summary = "채팅 읽음")
    public ApiResponse<Void> readChat(
            @AuthenticationPrincipal User user,
            @PathVariable String chattingId
    ) {

        chatService.readChat(user, chattingId);

        return ApiResponse.ok();
    }

    @GetMapping("/{meetingId}/sse")
    @Operation(summary = "SSE 터널 열기")
    public SseEmitter openSseTunnel(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId
    ) {

        return chatService.openSseTunnel(user, meetingId);
    }
}

