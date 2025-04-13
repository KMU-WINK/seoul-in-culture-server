package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.controller;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "[행사] [모임]")
public class MeetingController {

    private final MeetingService meetingService;

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
            @PathVariable String eventId,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) User.Gender gender
    ) {

        return ApiResponse.ok(meetingService.getMeetings(eventId, minAge, maxAge, gender));
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

    @PostMapping("/meeting/{meetingId}/manage/end")
    @Operation(summary = "[모임장] 모임 완료하기")
    public ApiResponse<GetMeetingResponse> endMeeting(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId
    ) {

        return ApiResponse.ok(meetingService.endMeeting(user, meetingId));
    }

    @PostMapping("/meeting/{meetingId}/manage/delegate/{targetId}")
    @Operation(summary = "[모임장] 모임장 위임하기")
    public ApiResponse<GetMeetingResponse> delegateHost(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId,
            @PathVariable String targetId
    ) {

        return ApiResponse.ok(meetingService.delegateHost(user, meetingId, targetId));
    }

    @DeleteMapping("/meeting/{meetingId}/manage")
    @Operation(summary = "[모임장] 모임 삭제하기")
    public ApiResponse<Void> deleteMeeting(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId
    ) {

        meetingService.deleteMeeting(user, meetingId);

        return ApiResponse.ok();
    }
}
