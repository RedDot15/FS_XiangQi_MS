package com.example.identity.mapper;

import com.example.identity.dto.request.RoleRequest;
import com.example.identity.dto.response.RoleResponse;
import com.example.identity.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissionEntities", ignore = true)
    RoleEntity toEntity(RoleRequest request);

    @Mapping(target = "permissionResponses", ignore = true)
    RoleResponse toResponse(RoleEntity role);
}
