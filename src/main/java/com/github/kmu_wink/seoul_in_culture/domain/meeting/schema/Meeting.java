package com.github.kmu_wink.seoul_in_culture.domain.meeting.schema;

import com.github.kmu_wink.seoul_in_culture.common.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Meeting extends BaseSchema {

    @DBRef
    Event event;

    String title;
    String description;

    LocalDateTime date;

    int maxPeople;

    @Nullable
    Integer minAge;

    @Nullable
    Integer maxAge;

    @Nullable
    User.Gender gender;

    @DBRef
    User host;

    @DBRef
    Set<User> participants;

    boolean end;
}