package com.example.matchContract.entity.redis;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractUserEntity {
    String id;

    Boolean acceptStatus;
}
