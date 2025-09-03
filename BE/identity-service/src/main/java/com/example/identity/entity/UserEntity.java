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
@Table(name = "user", schema = "xiangqi_ms", uniqueConstraints = {
        @UniqueConstraint(name = "username_UNIQUE", columnNames = {"username"}),
        @UniqueConstraint(name = "email_UNIQUE", columnNames = {"email"})
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Size(max = 16)
    @Column(name = "id", updatable = false, nullable = false, length = 16)
    UUID id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    String username;

    @Size(max = 75)
    @Column(name = "email", length = 75)
    String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    String password;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<RoleEntity> roleEntities = new LinkedHashSet<>();
}