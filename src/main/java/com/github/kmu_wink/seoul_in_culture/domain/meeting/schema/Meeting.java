package com.github.kmu_wink.seoul_in_culture.domain.meeting.schema;

import com.github.kmu_wink.seoul_in_culture.common.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class Meeting extends BaseSchema {

    @DBRef(lazy = true)
    @Indexed
    Event event;

    String title;
    String description;

    @Indexed
    LocalDateTime date;

    int maxPeople;

    @Nullable
    @Indexed
    Integer minAge;

    @Nullable
    @Indexed
    Integer maxAge;

    @Nullable
    @Indexed
    User.Gender gender;

    @DBRef(lazy = true)
    @Indexed
    User host;

    @DBRef(lazy = true)
    @Indexed
    Set<User> participants;

    boolean end;
}