package com.example.match.mapper;

import com.example.match.dto.response.MatchResponse;
import com.example.match.entity.my_sql.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchMapper {
	// Response
	@Mapping(target = "redUserResponse", source = "redPlayerEntity")
	@Mapping(target = "blackUserResponse", source = "blackPlayerEntity")
	MatchResponse toResponse(MatchEntity matchEntity);
}
