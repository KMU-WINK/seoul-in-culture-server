package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response.GetMyMeetingsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response.MyMeetingResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.service.MeetingService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@AuthGuard
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "[행사] [모임]")
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping("/meeting")
    @Operation(summary = "내 모임 목록")
    public ApiResponse<GetMyMeetingsResponse> getMyMeetings(
        @AuthenticationPrincipal User user,
        @RequestParam boolean end
    ) {

        return ApiResponse.ok(meetingService.getMyMeetings(user, end));
    }

    @PostMapping("/meeting/{meetingId}/manage/end")
    @Operation(summary = "[모임장] 모임 완료하기")
    public ApiResponse<MyMeetingResponse> endMeeting(
        @AuthenticationPrincipal User user,
        @PathVariable String meetingId
    ) {

        return ApiResponse.ok(meetingService.endMeeting(user, meetingId));
    }

    @PostMapping("/meeting/{meetingId}/manage/delegate/{targetId}")
    @Operation(summary = "[모임장] 모임장 위임하기")
    public ApiResponse<MyMeetingResponse> delegateHost(
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
