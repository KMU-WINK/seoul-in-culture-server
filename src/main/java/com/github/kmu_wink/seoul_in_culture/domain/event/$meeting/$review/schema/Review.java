package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.github.kmu_wink.seoul_in_culture.common.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseSchema {

	@DBRef
	Meeting meeting;

	@DBRef
	User author;

	@DBRef
	User target;

	int score;
	String content;
}