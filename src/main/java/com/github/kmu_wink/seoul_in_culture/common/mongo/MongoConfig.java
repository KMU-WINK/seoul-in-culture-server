package com.github.kmu_wink.seoul_in_culture.common.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    public static final Sort LATEST_SORT = Sort.by(Sort.Order.desc("createdAt"));
    public static final Sort OLDER_SORT = Sort.by(Sort.Order.asc("createdAt"));
}
