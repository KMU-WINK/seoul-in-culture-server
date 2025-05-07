package com.github.kmu_wink.seoul_in_culture.domain.meeting.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.request.CreateMeetingRequest;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.response.GetMeetingResponse;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.response.GetMeetingsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.service.MeetingService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
@Tag(name = "[모임]")
public class MeetingController {

    private final MeetingService meetingService;

    @AuthGuard
    @GetMapping
    @Operation(summary = "내 모임 목록 조회")
    public ApiResponse<GetMeetingsResponse> getMyMeetings(
            @AuthenticationPrincipal User user,
            @RequestParam boolean active
    ) {

        return ApiResponse.ok(meetingService.getMyMeetings(user, active));
    }

    @GetMapping("/list/{eventId}")
    @Operation(summary = "행사의 모임 목록 조회")
    public ApiResponse<GetMeetingsResponse> getMeetings(
            @PathVariable String eventId,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) User.Gender gender
    ) {

        return ApiResponse.ok(meetingService.getMeetings(eventId, minAge, maxAge, gender));
    }

    @GetMapping("/{meetingId}")
    @Operation(summary = "모임 상세 조회")
    public ApiResponse<GetMeetingResponse> getMeeting(
            @PathVariable String meetingId
    ) {

        return ApiResponse.ok(meetingService.getMeeting(meetingId));
    }

    @AuthGuard
    @PostMapping("/{eventId}/create")
    @Operation(summary = "모임 만들기")
    public ApiResponse<GetMeetingResponse> createMeeting(
            @AuthenticationPrincipal User user,
            @PathVariable String eventId,
            @RequestBody @Valid CreateMeetingRequest request
    ) {

        return ApiResponse.ok(meetingService.createMeeting(user, eventId, request));
    }

    @AuthGuard
    @PostMapping("/{meetingId}/join")
    @Operation(summary = "모임 참가")
    public ApiResponse<GetMeetingResponse> joinMeeting(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId,
            @RequestParam String orderId,
            @RequestParam String paymentKey,
            @RequestParam Integer amount
    ) {

        return ApiResponse.ok(meetingService.joinMeeting(user, meetingId, orderId, paymentKey, amount));
    }

    @AuthGuard
    @PostMapping("/{meetingId}/leave")
    @Operation(summary = "모임 나가기")
    public ApiResponse<GetMeetingResponse> leaveMeeting(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId
    ) {

        return ApiResponse.ok(meetingService.leaveMeeting(user, meetingId));
    }

    @AuthGuard
    @PatchMapping("/{meetingId}/finish")
    @Operation(summary = "[모임장] 모임 완료하기")
    public ApiResponse<GetMeetingResponse> finishMeeting(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId,
            @RequestParam List<String> attendant
    ) {

        return ApiResponse.ok(meetingService.finishMeeting(user, meetingId, attendant));
    }

    @AuthGuard
    @PatchMapping("/{meetingId}/delegate/{targetId}")
    @Operation(summary = "[모임장] 모임장 위임하기")
    public ApiResponse<GetMeetingResponse> delegateHost(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId,
            @PathVariable String targetId
    ) {

        return ApiResponse.ok(meetingService.delegateHost(user, meetingId, targetId));
    }

    @AuthGuard
    @DeleteMapping("/{meetingId}")
    @Operation(summary = "[모임장] 모임 삭제하기")
    public ApiResponse<Void> deleteMeeting(@AuthenticationPrincipal User user, @PathVariable String meetingId) {

        meetingService.deleteMeeting(user, meetingId);

        return ApiResponse.ok();
    }

    @AuthGuard
    @PostMapping("/{meetingId}/boost")
    @Operation(summary = "[모임장] 모임 부스트 하기")
    public ApiResponse<GetMeetingResponse> boostMeeting(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId,
            @RequestParam String orderId,
            @RequestParam String paymentKey,
            @RequestParam Integer amount
    ) {

        return ApiResponse.ok(meetingService.boostMeeting(user, meetingId, orderId, paymentKey, amount));
    }
}
