package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.auth.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.request.SendChatRequest;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.ChatListResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.RoomListResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.SendChatResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.service.ChatMessageService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@AuthGuard
@RestController
@RequestMapping("/event/meeting/chat")
@Tag(name = "[행사] [모임] [채팅]")
@RequiredArgsConstructor
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	@GetMapping
	@Operation(summary = "채팅방 목록")
	public ApiResponse<RoomListResponse> getRoomList(@AuthenticationPrincipal User user) {

		return ApiResponse.ok(chatMessageService.getRoomList(user));
	}

	@GetMapping("/{meetingId}")
	@Operation(summary = "채팅 기록")
	public ApiResponse<ChatListResponse> getChatList(@AuthenticationPrincipal User user, @PathVariable String meetingId) {

		return ApiResponse.ok(chatMessageService.getChatList(user, meetingId));
	}

	@PostMapping("/{meetingId}")
	@Operation(summary = "채팅 보내기")
	public ApiResponse<SendChatResponse> sendChat(@AuthenticationPrincipal User user, @PathVariable String meetingId, @RequestBody @Valid SendChatRequest dto) {

		return ApiResponse.ok(chatMessageService.sendChat(user, meetingId, dto));
	}

	@PostMapping("/read/all/{meetingId}")
	@Operation(summary = "채팅 모두 읽음")
	public ApiResponse<Void> readAllChat(@AuthenticationPrincipal User user, @PathVariable String meetingId) {

		chatMessageService.readAllChat(user, meetingId);

		return ApiResponse.ok();
	}

	@PostMapping("/read/{chattingId}")
	@Operation(summary = "채팅 읽음")
	public ApiResponse<Void> readChat(@AuthenticationPrincipal User user, @PathVariable String chattingId) {

		chatMessageService.readChat(user, chattingId);

		return ApiResponse.ok();
	}

	@GetMapping("/{meetingId}/sse")
	@Operation(summary = "SSE 터널 열기")
	public SseEmitter openSseTunnel(@AuthenticationPrincipal User user, @PathVariable String meetingId) {

		return chatMessageService.openSseTunnel(user, meetingId);
	}
}

