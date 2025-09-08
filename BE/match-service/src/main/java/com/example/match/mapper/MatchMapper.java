package com.example.match.mapper;

import com.example.match.dto.response.MatchResponse;
import com.example.match.entity.mongo.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchMapper {
	// Response
	@Mapping(target = "redUserResponse", ignore = true)
	@Mapping(target = "blackUserResponse", ignore = true)
	MatchResponse toResponse(MatchEntity matchEntity);
}
