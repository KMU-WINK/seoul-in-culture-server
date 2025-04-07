package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class MeetingResponse {
    private String id;
    private String title;
    private String description;
    private LocalDateTime date;
    private int maxPeople;
    private Integer minAge;
    private Integer maxAge;
    private User.Gender gender;
    private boolean isEnd;
    private boolean isHost;

    private String eventId;
    private String eventTitle;
    private String eventImage;
}