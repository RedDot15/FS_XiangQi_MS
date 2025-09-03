package com.example.invitation.dto.response;

import com.example.invitation.model.PlayerStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OnlineUserResponse {
    String displayedName;

    PlayerStatus status;

    String userId;

    Integer rating;
}
