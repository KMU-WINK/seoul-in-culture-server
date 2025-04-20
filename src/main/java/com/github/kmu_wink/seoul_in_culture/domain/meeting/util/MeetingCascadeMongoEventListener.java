package com.github.kmu_wink.seoul_in_culture.domain.meeting.util;

import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingCascadeMongoEventListener extends AbstractMongoEventListener<Meeting> {

    private final MongoTemplate mongoTemplate;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Meeting> event) {

        ObjectId meetingId = event.getSource().getObjectId("_id");

        System.out.println(mongoTemplate.remove(new Query(Criteria.where("meeting.$id").is(meetingId)), Chat.class));
        System.out.println(mongoTemplate.remove(new Query(Criteria.where("meeting.$id").is(meetingId)), Review.class));
        System.out.println(mongoTemplate.remove(
                new Query(Criteria.where("detail.meeting.$id").is(meetingId)),
                Notification.class
        ));
    }
}
