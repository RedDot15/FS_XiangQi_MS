package com.example.match.dto.request;

import com.example.match.dto.model.Position;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatchRequest {
    @NotNull(message = "User 1 ID is required.")
    String user1Id;

    @NotNull(message = "User 2 ID is required.")
    String user2Id;

    @NotNull(message = "Game mode is required.")
    Boolean isRank;
}
