package com.example.matchContract.entity.redis;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatchContractEntity {
    ContractUserEntity user1;

    ContractUserEntity user2;
}
