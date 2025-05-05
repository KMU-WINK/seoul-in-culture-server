package com.github.kmu_wink.seoul_in_culture.common.scheduler;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetingBoostScheduler {

    private final MeetingRepository meetingRepository;

    private static final Duration BoostDuration = Duration.ofHours(24);

    @Scheduled(cron = "0 * * * * *")
    public void clearExpiredBoostedMeetings() {
        LocalDateTime threshold = LocalDateTime.now().minus(BoostDuration);
        meetingRepository.clearExpiredBoosts(threshold);
    }
}
