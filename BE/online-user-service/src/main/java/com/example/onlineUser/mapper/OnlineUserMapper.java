package com.example.onlineUser.mapper;

import com.example.onlineUser.dto.response.OnlineUserResponse;
import com.example.onlineUser.entity.OnlineUser;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OnlineUserMapper {
    OnlineUserResponse toResponse(OnlineUser onlineUser);
}
