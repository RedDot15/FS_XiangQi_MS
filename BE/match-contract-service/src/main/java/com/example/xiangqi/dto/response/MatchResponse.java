package com.example.xiangqi.dto.response;

import jakarta.persistence.Column;
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
public class MatchResponse {
    Long id;

    PlayerResponse redPlayerResponse;

    PlayerResponse blackPlayerResponse;

    String result;

    Instant startTime;

    Instant endTime;
}
