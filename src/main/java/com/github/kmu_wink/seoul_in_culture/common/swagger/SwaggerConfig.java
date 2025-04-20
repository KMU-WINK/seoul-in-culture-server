package com.github.kmu_wink.seoul_in_culture.common.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI().components(new Components().addSecuritySchemes("JWT", securityScheme()))
                .info(info())
                .servers(server());
    }

    private Info info() {

        return new Info().title("Seoul IN Culture").description("2025 서울 열린데이터광장 공모전").version("1.0.0");
    }

    private SecurityScheme securityScheme() {

        return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
    }

    private List<Server> server() {

        Server server1 = new Server();
        server1.setUrl("https://seoul-in-culture.daehyeon.cloud/api");

        Server server2 = new Server();
        server2.setUrl("http://localhost:8080/api");

        return List.of(server1, server2);
    }
}