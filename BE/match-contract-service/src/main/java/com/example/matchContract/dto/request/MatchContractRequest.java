package com.example.matchContract.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatchContractRequest {
    ContractUserRequest player1;

    ContractUserRequest player2;
}
