package com.example.matchContract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MatchContractApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchContractApplication.class, args);
    }
}
