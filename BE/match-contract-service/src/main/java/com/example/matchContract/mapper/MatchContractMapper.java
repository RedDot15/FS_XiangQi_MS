package com.example.matchContract.mapper;

import com.example.matchContract.dto.request.MatchContractRequest;
import com.example.matchContract.dto.request.PlayerRequest;
import com.example.matchContract.dto.response.PlayerResponse;
import com.example.matchContract.entity.my_sql.PlayerEntity;
import com.example.matchContract.entity.redis.MatchContractEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchContractMapper {
	// Add
	@Mapping(target = "player1", source = "player1")
	@Mapping(target = "player2", source = "player2")
	MatchContractEntity toEntity(MatchContractRequest request);
}
