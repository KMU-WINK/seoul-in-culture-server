package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.request.CreateMeetingRequest;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response.GetMeetingResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response.GetMeetingsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.service.MeetingService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "[행사] [모임]")
public class MeetingController {

	private final MeetingService meetingService;

	// ?active=true : 진행중인 모임 , 지난 모임
	@AuthGuard
	@GetMapping("/meeting")
	@Operation(summary = "내 모임 목록 조회")
	public ApiResponse<GetMeetingsResponse> getMyMeetings(
		@AuthenticationPrincipal User user,
		@RequestParam boolean active
	) {

		return ApiResponse.ok(meetingService.getMyMeetings(user, active));
	}

	@GetMapping("/{eventId}/meeting")
	@Operation(summary = "행사의 모임 목록 조회")
	public ApiResponse<GetMeetingsResponse> getMeetings(
		@PathVariable String eventId
	) {

		return ApiResponse.ok(meetingService.getMeetings(eventId));
	}

	@GetMapping("/meeting/{meetingId}")
	@Operation(summary = "모임 상세 조회")
	public ApiResponse<GetMeetingResponse> getMeeting(
		@PathVariable String meetingId
	) {

		return ApiResponse.ok(meetingService.getMeeting(meetingId));
	}

	@AuthGuard
	@PostMapping("/{eventId}/meeting")
	@Operation(summary = "모임 만들기")
	public ApiResponse<GetMeetingResponse> createMeeting(
		@AuthenticationPrincipal User user,
		@PathVariable String eventId,
		@RequestBody @Valid CreateMeetingRequest request
	) {

		return ApiResponse.ok(meetingService.createMeeting(user, eventId, request));
	}

	@AuthGuard
	@PostMapping("/meeting/{meetingId}")
	@Operation(summary = "모임 참가")
	public ApiResponse<Void> joinMeeting(
		@AuthenticationPrincipal User user,
		@PathVariable String meetingId
	) {

		meetingService.joinMeeting(user, meetingId);

		return ApiResponse.ok();
	}

	@AuthGuard
	@DeleteMapping("/meeting/{meetingId}")
	@Operation(summary = "모임 나가기")
	public ApiResponse<Void> leaveMeeting(
		@AuthenticationPrincipal User user,
		@PathVariable String meetingId
	) {

		meetingService.leaveMeeting(user, meetingId);

		return ApiResponse.ok();
	}
}
