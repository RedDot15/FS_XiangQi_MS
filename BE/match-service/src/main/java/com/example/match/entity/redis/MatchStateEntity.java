package com.example.match.entity.redis;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatchStateEntity {
    String[][] boardState;

    MatchStateUserEntity redUser;

    MatchStateUserEntity blackUser;

    String turn;

    Instant lastMoveTime;

    String mode;
}
