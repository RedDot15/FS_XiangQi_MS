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
public class MatchResponse {
    String id;

    ProfileResponse redUserResponse;

    ProfileResponse blackUserResponse;

    String result;

    Instant startTime;

    Instant endTime;
}
