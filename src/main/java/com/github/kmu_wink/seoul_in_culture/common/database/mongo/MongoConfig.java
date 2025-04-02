package com.github.kmu_wink.seoul_in_culture.common.database.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableMongoAuditing
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableMongoRepositories(
	basePackages = "com.github.kmu_wink.seoul_in_culture",
	excludeFilters = @ComponentScan.Filter(
		type = FilterType.REGEX,
		pattern = ".*\\.repository\\..*Redis.*"
	)
)
public class MongoConfig {

	@Bean
	public MappingMongoConverter mappingMongoConverter(
		MongoDatabaseFactory mongoDatabaseFactory,
		MongoMappingContext mongoMappingContext
	) {

		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
		MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));

		return converter;
	}
}
