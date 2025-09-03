package com.example.match.dto.request;

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
public class ResignRequest {
    @NotNull(message = "Match ID is required.")
    String matchId;

    @NotNull(message = "Surrender ID is required.")
    String surrenderId;
}
