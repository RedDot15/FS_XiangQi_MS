package com.example.chat.controller.ws;

import com.example.chat.dto.request.ChatRequest;
import com.example.chat.dto.response.ChatMessage;
import com.example.chat.helper.MessageObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class ChatWSController {
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendChat(ChatRequest request) {
        // Send chat message
        messagingTemplate.convertAndSend(
                "/topic/match/" + request.getMatchId() + "/chat",
                new MessageObject(
                        "Chat message received.",
                        new ChatMessage(
                                request.getSender(),
                                request.getMessage(),
                                Instant.now())));
    }
}