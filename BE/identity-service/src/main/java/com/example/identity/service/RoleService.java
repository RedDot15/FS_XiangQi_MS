package com.example.identity.service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.identity.dto.request.RoleRequest;
import com.example.identity.dto.response.RoleResponse;
import com.example.identity.entity.PermissionEntity;
import com.example.identity.entity.RoleEntity;
import com.example.identity.mapper.RoleMapper;
import com.example.identity.repository.my_sql.PermissionRepository;
import com.example.identity.repository.my_sql.RoleRepository;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        // Mapping request -> entity
        RoleEntity roleEntity = roleMapper.toEntity(request);
        // Get permission entities
        List<PermissionEntity> permissionEntities = permissionRepository.findAllById(
                request.getPermissionIds()
                    .stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toCollection(HashSet::new)));
        // Set permission entities
        roleEntity.setPermissionEntities(new HashSet<>(permissionEntities));
        // Save & Return new role
        return roleMapper.toResponse(roleRepository.save(roleEntity));
    }

    public List<RoleResponse> getAll() {
        // Get all roles
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    public String delete(String roleId) {
        // Delete
        roleRepository.deleteById(UUID.fromString(roleId));
        // Return id
        return roleId;
    }
}
