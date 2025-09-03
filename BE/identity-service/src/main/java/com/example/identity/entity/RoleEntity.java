package com.example.identity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "role", schema = "xiangqi_ms")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Size(max = 16)
    @Column(name = "id", updatable = false, nullable = false, length = 16)
    UUID id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    String description;

    @ManyToMany
    @JoinTable(name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    Set<PermissionEntity> permissionEntities = new LinkedHashSet<>();

}