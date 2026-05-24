package com.agroerp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class AgroBusinessErpApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgroBusinessErpApplication.class, args);
    }
}
