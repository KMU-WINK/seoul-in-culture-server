package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.auth.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.service.MeetingService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AuthGuard
@RestController
@RequestMapping("/event/{eventId}/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @DeleteMapping("/{meetingId}/leave")
    public ApiResponse<Void> leaveMeeting(@AuthenticationPrincipal User user, @PathVariable String meetingId) {

        meetingService.leaveMeeting(user, meetingId);
        return ApiResponse.ok();
    }


}
