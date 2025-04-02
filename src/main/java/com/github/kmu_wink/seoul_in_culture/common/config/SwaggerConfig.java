package com.github.kmu_wink.seoul_in_culture.common.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String SWAGGER_AUTH = "JWT";

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("여기차대")
                .version("v0.0.1")
                .description("2025 서울 열린데이터광장 공모전");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
