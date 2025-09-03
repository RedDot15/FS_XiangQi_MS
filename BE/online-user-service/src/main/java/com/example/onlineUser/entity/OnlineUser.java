package com.example.onlineUser.entity;

import com.example.onlineUser.model.PlayerStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OnlineUser {
    String displayedName;

    @NonFinal
    @Setter
    PlayerStatus status;

    String userId;

    Integer rating;
}