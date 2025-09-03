package com.example.invitation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InvitationApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvitationApplication.class, args);
    }

}
