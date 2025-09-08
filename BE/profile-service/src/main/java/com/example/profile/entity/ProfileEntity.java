package com.example.profile.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    UUID id;

    @Property("userId")
    @NotNull
    String userId;

    @NotNull
    String displayedName;

    @NotNull
    Integer rating;
}