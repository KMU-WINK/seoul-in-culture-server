package com.github.kmu_wink.seoul_in_culture.common.property;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.cloud.aws")
public class AwsProperty {

    @NotBlank
    private String region;

    @NestedConfigurationProperty
    private S3 s3;

    @NestedConfigurationProperty
    private Credential credential;

    @Data
    public static class S3 {

        @NotBlank
        private String bucket;
    }

    @Data
    public static class Credential {

        @NotBlank
        private String accessKey;

        @NotBlank
        private String secretKey;
    }
}