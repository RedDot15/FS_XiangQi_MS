package com.example.match.entity.redis;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatchStateUserEntity {
    String id;

    String name;

    Integer rating;

    Long totalTimeLeft;
}
