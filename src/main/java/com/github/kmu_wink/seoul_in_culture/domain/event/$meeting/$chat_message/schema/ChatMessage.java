package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.github.kmu_wink.seoul_in_culture.common.database.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends BaseSchema {

	@DBRef
	Meeting meeting;

	@DBRef
	User user;

	String content;

	@DBRef
	Set<User> read;
}