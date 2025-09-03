package com.example.invitation.controller.ws;

import com.example.invitation.dto.request.InvitationAcceptRequest;
import com.example.invitation.dto.request.InvitationCreateRequest;
import com.example.invitation.dto.request.InvitationRejectRequest;
import com.example.invitation.dto.request.InvitationRetrieveRequest;
import com.example.invitation.service.InvitationService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class InvitationWSController {
    InvitationService invitationService;

    @MessageMapping("/invitation.create")
    public void createInvitation(@Valid InvitationCreateRequest request) {
        // Send invitation
        invitationService.invite(request);
    }

    @MessageMapping("/invitation.accept")
    public void acceptInvitation(@Valid InvitationAcceptRequest request) {
        // Accept invitation
        invitationService.acceptInvitation(request);
    }

    @MessageMapping("/invitation.reject")
    public void rejectInvitation(@Valid InvitationRejectRequest request) {
        // Reject invitation
        invitationService.rejectInvitation(request);
    }

    @MessageMapping("/invitation.retrieve")
    public void retrieveInvitation(@Valid InvitationRetrieveRequest request) {
        // Retrieve invitation
        invitationService.retrieveInvitation(request);
    }
}