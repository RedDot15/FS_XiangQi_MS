package com.example.onlineUser.controller.ws;

import com.example.onlineUser.service.OnlineUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class OnlineUserWSController {
    OnlineUserService onlineUserService;

    @MessageMapping("/status")
    public void updateStatus(String payload, StompHeaderAccessor headerAccessor) {
        // Update status
        onlineUserService.updateStatus(payload, headerAccessor);
    }
}