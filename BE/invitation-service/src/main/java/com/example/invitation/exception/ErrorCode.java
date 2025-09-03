package com.example.invitation.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // General
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized error."),
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),
    // Authentication
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Unauthenticated error."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "You do not have permission to perform this operation."),
    // Invite
    OPPONENT_STATUS_IN_MATCH(HttpStatus.BAD_REQUEST, "This player is in match."),
    OPPONENT_STATUS_QUEUE(HttpStatus.BAD_REQUEST, "This player is in queue."),
    INVITATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "This invitation already exists."),
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "This invitation does not exist."),
    ;

    HttpStatus httpStatus;
    String message;
}
