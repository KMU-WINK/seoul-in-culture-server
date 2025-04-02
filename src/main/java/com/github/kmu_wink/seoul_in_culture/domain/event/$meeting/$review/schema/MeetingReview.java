package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.github.kmu_wink.seoul_in_culture.common.database.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MeetingReview extends BaseSchema {

	@DBRef
	Meeting meeting;

	@DBRef
	MeetingParticipant author;

	@DBRef
	MeetingParticipant target;

	int score;
	String content;
}