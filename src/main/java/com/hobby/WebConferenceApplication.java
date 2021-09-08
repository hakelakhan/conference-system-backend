package com.hobby;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WebConferenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebConferenceApplication.class, args);
    }
}
