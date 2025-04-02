package com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.github.kmu_wink.seoul_in_culture.common.database.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Bookmark extends BaseSchema {

	@DBRef
	User user;

	@DBRef
	Event event;
}