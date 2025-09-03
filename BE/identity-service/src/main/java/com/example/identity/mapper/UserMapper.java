package com.example.identity.mapper;

import com.example.identity.dto.request.UserRequest;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
	// Add
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "roleEntities", ignore = true)
	UserEntity toEntity(UserRequest userRequest);

	// Response
	@Mapping(target = "id", ignore = true)
	UserResponse toResponse(UserEntity entity);
}
