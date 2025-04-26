package com.github.kmu_wink.seoul_in_culture.domain.chat.schema;

import com.github.kmu_wink.seoul_in_culture.common.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class Chat extends BaseSchema {

    @DBRef(lazy = true)
    @Indexed
    Meeting meeting;

    @DBRef(lazy = true)
    @Indexed
    User user;

    String content;

    @DBRef(lazy = true)
    @Indexed
    Set<User> unread;
}