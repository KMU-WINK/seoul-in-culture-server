package com.github.kmu_wink.yeogichadae2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Yeogichadae2Application {

    public static void main(String[] args) {
        SpringApplication.run(Yeogichadae2Application.class, args);
    }
}
