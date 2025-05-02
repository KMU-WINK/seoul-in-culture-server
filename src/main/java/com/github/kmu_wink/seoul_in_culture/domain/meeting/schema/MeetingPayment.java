package com.github.kmu_wink.seoul_in_culture.domain.meeting.schema;

import com.github.kmu_wink.seoul_in_culture.common.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class MeetingPayment extends BaseSchema {

    @DBRef(lazy = true)
    Meeting meeting;

    @DBRef(lazy = true)
    User user;

    String paymentKey;
}