package com.robertciotoiu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MobileDeScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileDeScraperApplication.class, args);}
}
