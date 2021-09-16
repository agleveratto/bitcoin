package com.agleveratto.bitcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BitcoinSpringWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitcoinSpringWebApplication.class, args);
    }

}
