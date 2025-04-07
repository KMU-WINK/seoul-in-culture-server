package com.github.kmu_wink.seoul_in_culture.common.security.authentication;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.kmu_wink.seoul_in_culture.common.swagger.SwaggerConfig;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = SwaggerConfig.SWAGGER_AUTH)
public @interface AuthGuard {
}
