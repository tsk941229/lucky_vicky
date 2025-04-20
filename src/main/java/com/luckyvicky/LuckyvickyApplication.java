package com.luckyvicky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LuckyvickyApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyvickyApplication.class, args);
    }

}
