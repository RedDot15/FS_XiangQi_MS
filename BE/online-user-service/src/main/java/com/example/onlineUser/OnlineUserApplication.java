package com.example.onlineUser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OnlineUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineUserApplication.class, args);
    }
}
