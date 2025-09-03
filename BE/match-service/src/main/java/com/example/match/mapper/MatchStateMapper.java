package com.example.match.mapper;

import com.example.match.dto.response.MatchStateResponse;
import com.example.match.entity.redis.MatchStateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchStateMapper {
	// Add
	@Mapping(target = "redPlayer", source = "redPlayer")
	@Mapping(target = "blackPlayer", source = "blackPlayer")
	MatchStateResponse toResponse(MatchStateEntity entity);
}
