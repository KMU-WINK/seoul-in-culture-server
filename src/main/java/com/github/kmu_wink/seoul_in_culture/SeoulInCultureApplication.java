package com.github.kmu_wink.seoul_in_culture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@EnableAsync
@SpringBootApplication
public class SeoulInCultureApplication {

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        SpringApplication.run(SeoulInCultureApplication.class, args);
    }
}
