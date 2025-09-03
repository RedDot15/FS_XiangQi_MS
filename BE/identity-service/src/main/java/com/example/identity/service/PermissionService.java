package com.example.identity.service;

import java.util.List;
import java.util.UUID;

import com.example.identity.dto.request.PermissionRequest;
import com.example.identity.dto.response.PermissionResponse;
import com.example.identity.entity.PermissionEntity;
import com.example.identity.mapper.PermissionMapper;
import com.example.identity.repository.my_sql.PermissionRepository;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        // Mapping request -> entity
        PermissionEntity entity = permissionMapper.toEntity(request);
        // Save & Return new permission
        return permissionMapper.toResponse(permissionRepository.save(entity));
    }

    public List<PermissionResponse> getAll() {
        // Get all permission
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toResponse)
                .toList();
    }

    public String delete(String permissionId) {
        // Delete
        permissionRepository.deleteById(UUID.fromString(permissionId));
        // Return id
        return permissionId;
    }
}
