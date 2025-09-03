package com.example.match.exception;

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
    // Authentication
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Unauthenticated error."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "You do not have permission to perform this operation."),
    // Match
    MATCH_NOT_FOUND(HttpStatus.NOT_FOUND, "Match not found."),
    // Board State
    BOARD_STATE_SERIALIZED_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialize board state"),
    BOARD_STATE_PARSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing board state from Redis"),
    // Move
    INVALID_MOVE(HttpStatus.BAD_REQUEST, "Invalid move."),
    ;

    HttpStatus httpStatus;
    String message;
}
