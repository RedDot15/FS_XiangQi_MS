package com.example.identity.mapper;

import com.example.identity.dto.request.PermissionRequest;
import com.example.identity.dto.response.PermissionResponse;
import com.example.identity.entity.PermissionEntity;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    @Mapping(target = "id", ignore = true)
    PermissionEntity toEntity(PermissionRequest request);

    PermissionResponse toResponse(PermissionEntity permission);
}
