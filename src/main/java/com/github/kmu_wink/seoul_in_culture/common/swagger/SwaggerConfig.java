package com.github.kmu_wink.seoul_in_culture.common.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String SWAGGER_AUTH = "JWT";

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("Seoul IN Culture")
                .version("v1.0.0")
                .description("2025 서울 열린데이터광장 공모전");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
