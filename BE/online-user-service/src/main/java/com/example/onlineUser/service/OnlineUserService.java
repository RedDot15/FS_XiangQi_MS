package com.example.onlineUser.service;


import com.example.onlineUser.dto.response.OnlineUserResponse;
import com.example.onlineUser.dto.response.ProfileResponse;
import com.example.onlineUser.entity.OnlineUser;
import com.example.onlineUser.exception.AppException;
import com.example.onlineUser.exception.ErrorCode;
import com.example.onlineUser.mapper.OnlineUserMapper;
import com.example.onlineUser.model.PlayerStatus;
import com.example.onlineUser.repository.ProfileClient;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class OnlineUserService {
    // Maps to store session and player information
    @NonFinal
    Map<String, OnlineUser> sessionToUser = new ConcurrentHashMap<>();
    @NonFinal
    Map<String, OnlineUser> displayedNameToUser = new ConcurrentHashMap<>();

    ProfileClient profileClient;
    OnlineUserMapper onlineUserMapper;

    public void updateStatus(String payload, StompHeaderAccessor headerAccessor) {
        String[] parts = payload.replaceAll("\"", "").split(":");
        if (parts.length >= 4) {
            String userId = parts[1];
            String command = parts[2];
            String status = parts[3];
            String sessionId = headerAccessor.getSessionId();

            // Get player
            ProfileResponse profileResponse = (ProfileResponse) profileClient.getById(userId).getData();
            // Get player username
            String displayedName = profileResponse.getDisplayedName();
            // Get status
            PlayerStatus playerStatus = PlayerStatus.valueOf(status);

            if ("STATUS".equals(command)) {
                // Get playerInfo
                OnlineUser onlineUser = sessionToUser.get(sessionId);
                if (onlineUser == null) {
                    // Define playerInfo
                    onlineUser = new OnlineUser(displayedName, playerStatus, userId, profileResponse.getRating());
                    // Save new player session to list
                    sessionToUser.put(sessionId, onlineUser);
                    displayedNameToUser.put(displayedName, onlineUser);
                } else {
                    // Set new status
                    onlineUser.setStatus(playerStatus);
                }
            }
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        // Get session ID
        String sessionId = event.getSessionId();
        // Remove session
        OnlineUser onlineUser = sessionToUser.remove(sessionId);
        if (onlineUser != null) {
            displayedNameToUser.remove(onlineUser.getDisplayedName());
        }
    }

    public OnlineUserResponse getByDisplayedName(String displayedName) {
        // Get context
        SecurityContext context = SecurityContextHolder.getContext();
        String myUsername = context.getAuthentication().getName();
        // Self finding exception
        if (displayedName.equals(myUsername))
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        // Find player in session list
        OnlineUser onlineUser = displayedNameToUser.get(displayedName);
        // Not found exception
        if (onlineUser == null)
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        // Return
        return onlineUserMapper.toResponse(onlineUser);
    }
}
