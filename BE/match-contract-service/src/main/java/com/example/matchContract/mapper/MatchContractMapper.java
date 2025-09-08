package com.example.matchContract.mapper;

import com.example.matchContract.dto.request.MatchContractRequest;
import com.example.matchContract.entity.redis.MatchContractEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchContractMapper {
	// Add
	@Mapping(target = "user1", source = "user1")
	@Mapping(target = "user2", source = "user2")
	MatchContractEntity toEntity(MatchContractRequest request);
}
