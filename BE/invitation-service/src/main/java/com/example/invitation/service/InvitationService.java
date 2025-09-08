package com.example.invitation.service;


import com.example.invitation.dto.request.*;
import com.example.invitation.dto.response.OnlineUserResponse;
import com.example.invitation.exception.AppException;
import com.example.invitation.exception.ErrorCode;
import com.example.invitation.helper.MessageObject;
import com.example.invitation.model.PlayerStatus;
import com.example.invitation.repository.http_client.MatchClient;
import com.example.invitation.repository.http_client.OnlineUserClient;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class InvitationService {
    // Maps to store invitations
    Map<String, List<String>> invitations = new ConcurrentHashMap<>();

    SimpMessagingTemplate messagingTemplate;
    OnlineUserClient onlineUserClient;
    MatchClient matchClient;

    public void invite(InvitationCreateRequest request) {
        // Find player in session list
        OnlineUserResponse inviteeOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviteeUsername()).getData();
        OnlineUserResponse inviterOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviterUsername()).getData();
        // Not found exception
        if (inviteeOnlineUser == null)
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        // Opponent status invalid
        if (inviteeOnlineUser.getStatus().equals(PlayerStatus.IN_MATCH))
            throw new AppException(ErrorCode.OPPONENT_STATUS_IN_MATCH);
        if (inviteeOnlineUser.getStatus().equals(PlayerStatus.QUEUE))
            throw new AppException(ErrorCode.OPPONENT_STATUS_QUEUE);

        // Save invitation
        // Get invitee's invitation list or create new one
        invitations.computeIfAbsent(request.getInviterUsername(), k -> new CopyOnWriteArrayList<>());
        // Invitation exists exception
        if (invitations.get(request.getInviterUsername()).contains(request.getInviteeUsername())) {
            throw new AppException(ErrorCode.INVITATION_ALREADY_EXISTS); // Ngăn gửi lời mời trùng
        }
        // Add the invitation
        invitations.get(request.getInviterUsername()).add(request.getInviteeUsername());

        // Send invitation creation message
        messagingTemplate.convertAndSend("/topic/invite/player/" + inviterOnlineUser.getUserId(),
                new MessageObject("To-inviter: Your invitation sent.", inviteeOnlineUser.getDisplayedName()));
        messagingTemplate.convertAndSend("/topic/invite/player/" + inviteeOnlineUser.getUserId(),
                new MessageObject("To-invitee: A new invitation received.", inviterOnlineUser.getDisplayedName()));
    }

    public void acceptInvitation(InvitationAcceptRequest request) {
        // Get invitation list
        List<String> opponentUsernameList = invitations.get(request.getInviterUsername());
        // Remove invitation
        boolean invitationExists = opponentUsernameList.remove(request.getInviteeUsername());
        // Invitation not found exception
        if (!invitationExists)
            throw new AppException(ErrorCode.INVITATION_NOT_FOUND);

        // Get player info
        OnlineUserResponse inviterOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviterUsername()).getData();
        OnlineUserResponse inviteeOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviteeUsername()).getData();

        // Create match
        String matchId = (String) matchClient.createMatch(MatchRequest.builder()
                .user1Id(inviterOnlineUser.getUserId())
                .user2Id(inviteeOnlineUser.getUserId())
                .isRank(false)
                .build()).getData();

        // Send invitation accept message
        messagingTemplate.convertAndSend("/topic/invite/player/" + inviterOnlineUser.getUserId(),
                new MessageObject("To-inviter: Your invitation accepted.", matchId));
        messagingTemplate.convertAndSend("/topic/invite/player/" + inviteeOnlineUser.getUserId(),
                new MessageObject("To-invitee: Accept invitation success.", matchId));
    }

    public void rejectInvitation(InvitationRejectRequest request) {
        if (request.getInviterUsername() == null != request.getInviterUsername().isEmpty()) {
            for (String inviterUsername : invitations.keySet()) {
                List<String> opponentUsernameList = invitations.get(inviterUsername);
                if (opponentUsernameList.contains(request.getInviteeUsername())) {
                    // Remove invitation
                    opponentUsernameList.remove(request.getInviteeUsername());
                    // Get player info
                    OnlineUserResponse inviterOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(inviterUsername).getData();
                    OnlineUserResponse inviteeOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviteeUsername()).getData();

                    // Send invitation reject message
                    messagingTemplate.convertAndSend("/topic/invite/player/" + inviterOnlineUser.getUserId(),
                            new MessageObject("To-inviter: Your invitation rejected.", inviteeOnlineUser.getDisplayedName()));
                    messagingTemplate.convertAndSend("/topic/invite/player/" + inviteeOnlineUser.getUserId(),
                            new MessageObject("To-invitee: Reject invitation success.", inviterOnlineUser.getDisplayedName()));
                }
            }
            // Return
            return;
        }

        // Get invitation list
        List<String> inviteeUsernameList = invitations.get(request.getInviterUsername());
        // Remove invitation
        boolean invitationExists = inviteeUsernameList.remove(request.getInviteeUsername());
        // Invitation not found exception
        if (!invitationExists)
            throw new AppException(ErrorCode.INVITATION_NOT_FOUND);

        // Get player info
        OnlineUserResponse inviterOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviterUsername()).getData();
        OnlineUserResponse inviteeOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviteeUsername()).getData();

        // Send invitation reject message
        messagingTemplate.convertAndSend("/topic/invite/player/" + inviterOnlineUser.getUserId(),
                new MessageObject("To-inviter: Your invitation rejected.", inviteeOnlineUser.getDisplayedName()));
        messagingTemplate.convertAndSend("/topic/invite/player/" + inviteeOnlineUser.getUserId(),
                new MessageObject("To-invitee: Reject invitation success.", inviterOnlineUser.getDisplayedName()));
    }


    public void retrieveInvitation(InvitationRetrieveRequest request) {
        // Get invitation list
        List<String> inviteeUsernameList = invitations.get(request.getInviterUsername());
        if (inviteeUsernameList == null) return;

        // Delete every invitation if username null
        if (request.getInviteeUsername() == null || request.getInviteeUsername().isEmpty()) {
            for (String inviteeUsername : inviteeUsernameList) {
                // Remove invitation
                inviteeUsernameList.remove(inviteeUsername);
                // Get player info
                OnlineUserResponse inviteeOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(inviteeUsername).getData();
                OnlineUserResponse inviterOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviterUsername()).getData();

                // Send invitation cancel message
                messagingTemplate.convertAndSend("/topic/invite/player/" + inviterOnlineUser.getUserId(),
                        new MessageObject("To-inviter: Retrieve invitation success.", inviteeOnlineUser.getDisplayedName()));
                messagingTemplate.convertAndSend("/topic/invite/player/" + inviteeOnlineUser.getUserId(),
                        new MessageObject("To-invitee: Invitation retrieved.", inviterOnlineUser.getDisplayedName()));
            }
            // Return
            return ;
        }

        // Remove invitation
        inviteeUsernameList.remove(request.getInviteeUsername());
        // Get player info
        OnlineUserResponse inviteeOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviteeUsername()).getData();
        OnlineUserResponse inviterOnlineUser = (OnlineUserResponse) onlineUserClient.getByUsername(request.getInviterUsername()).getData();

        // Send invitation cancel message
        messagingTemplate.convertAndSend("/topic/invite/player/" + inviterOnlineUser.getUserId(),
                new MessageObject("To-inviter: Retrieve invitation success.", inviteeOnlineUser.getDisplayedName()));
        messagingTemplate.convertAndSend("/topic/invite/player/" + inviteeOnlineUser.getUserId(),
                new MessageObject("To-invitee: Invitation retrieved.", inviterOnlineUser.getDisplayedName()));
    }
}
