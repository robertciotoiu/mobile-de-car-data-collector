package com.robertciotoiu.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication(scanBasePackages = "com.robertciotoiu")
@ComponentScan("com.robertciotoiu")
@EnableMongoRepositories(basePackages = "com.robertciotoiu")
@EntityScan("com.robertciotoiu")
@EnableAutoConfiguration
public class MobileDeDataApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileDeDataApiApplication.class, args);
    }
}
