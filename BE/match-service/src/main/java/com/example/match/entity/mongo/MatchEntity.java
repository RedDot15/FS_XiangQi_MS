package com.example.match.entity.mongo;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "matchs")
public class MatchEntity {
    @Id
    UUID id;

    @Field("red_user_id")
    String redUserId;

    @Field("black_user_id")
    String blackUserId;

    String result;

    Instant startTime;

    Instant endTime;
}