package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.auth.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.MeetingResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingNotFoundException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.service.MeetingService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AuthGuard
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @DeleteMapping("/{eventId}/meeting/{meetingId}/leave")
    public ApiResponse<Void> leaveMeeting(@AuthenticationPrincipal User user, @PathVariable String meetingId) {

        meetingService.leaveMeeting(user, meetingId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{eventId}/meeting/{meetingId}/manage/delete")
    public ApiResponse<Void> deleteMeeting(
            @AuthenticationPrincipal User user,
            @PathVariable String meetingId
    ) {
        meetingService.deleteMeeting(user, meetingId);
        return ApiResponse.ok();
    }

    @PostMapping("/{eventId}/meeting/{meetingId}/manage/end")
    public ApiResponse<Void> endMeeting(@AuthenticationPrincipal User user, @PathVariable String meetingId) {
        meetingService.endMeeting(user, meetingId);
        return ApiResponse.ok();
    }

    @PostMapping("/{eventId}/meeting/{meetingId}/manage/delegate/{userId}")
    public ApiResponse<Void> delegateHost(@AuthenticationPrincipal User currentHost, @PathVariable String meetingId, @PathVariable String userId) {
        meetingService.delegateHost(currentHost, userId, meetingId);
        return ApiResponse.ok();
    }

    @GetMapping("/meeting")
    public ApiResponse<List<MeetingResponse>> findMyMeetings(@AuthenticationPrincipal User user, @RequestParam(value = "active", required = false) Boolean active){
        List<MeetingResponse> meetingResponses = meetingService.findMyMeetings(user,active);
        return ApiResponse.ok(meetingResponses);
    }



}
