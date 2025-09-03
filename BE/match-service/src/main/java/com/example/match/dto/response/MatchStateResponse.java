package com.example.match.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatchStateResponse {
    String[][] boardState;

    MatchStatePlayerResponse redPlayer;

    MatchStatePlayerResponse blackPlayer;

    Long turn;

    Instant lastMoveTime;
}
