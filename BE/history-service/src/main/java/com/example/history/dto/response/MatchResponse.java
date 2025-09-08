package com.example.history.dto.response;

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

    ProfileResponse redUserResponse;

    ProfileResponse blackUserResponse;

    String result;

    Instant startTime;

    Instant endTime;
}
