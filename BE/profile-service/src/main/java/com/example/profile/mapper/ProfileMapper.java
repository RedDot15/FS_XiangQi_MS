package com.example.profile.mapper;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.response.ProfileResponse;
import com.example.profile.entity.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileMapper {
	// Add
	@Mapping(target = "rating", ignore = true)
	ProfileEntity toEntity(ProfileCreationRequest request);

	// Response
	ProfileResponse toResponse(ProfileEntity entity);
}
