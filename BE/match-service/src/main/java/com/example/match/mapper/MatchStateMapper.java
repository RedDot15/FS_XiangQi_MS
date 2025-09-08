package com.example.match.mapper;

import com.example.match.dto.response.MatchStateResponse;
import com.example.match.entity.redis.MatchStateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchStateMapper {
	// Add
	@Mapping(target = "redUser", source = "redUser")
	@Mapping(target = "blackUser", source = "blackUser")
	MatchStateResponse toResponse(MatchStateEntity entity);
}
