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
public class MoveRequest {
    @NotNull(message = "Match ID is required.")
    String matchId;

    @NotNull(message = "Mover ID is required.")
    String moverId;

    @NotNull(message = "From is required.")
    Position from;

    @NotNull(message = "To is required.")
    Position to;
}
