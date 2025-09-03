package com.example.identity.service;

import com.example.identity.dto.request.ChangePasswordRequest;
import com.example.identity.dto.request.ProfileCreationRequest;
import com.example.identity.dto.request.UserRequest;
import com.example.identity.dto.request.SocialPlayerRequest;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.entity.UserEntity;
import com.example.identity.exception.AppException;
import com.example.identity.exception.ErrorCode;
import com.example.identity.mapper.UserMapper;
import com.example.identity.repository.http_client.ProfileClient;
import com.example.identity.repository.my_sql.RoleRepository;
import com.example.identity.repository.my_sql.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Service
public class UserService {
	UserRepository userRepository;
	UserMapper userMapper;
	PasswordEncoder passwordEncoder;
	RedisService redisService;
	RoleRepository roleRepository;
	ProfileClient profileClient;

	public static final String ROLE_PLAYER = "PLAYER";

	public UserResponse getMyInfo() {
		// Get context
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		// Fetch
		UserEntity entity = userRepository
				.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		// Return
		return userMapper.toResponse(entity);
	}

	public UserResponse register(UserRequest userRequest) {
		// Mapping userRequest -> userEntity
		UserEntity entity = userMapper.toEntity(userRequest);
		// Encode password
		entity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		// Set default role
		if (entity.getRoleEntities().isEmpty())
			entity.setRoleEntities(Set.of(roleRepository.getReferenceByName(ROLE_PLAYER)));
		// Add
		try {
			entity = userRepository.save(entity);
		} catch (DataIntegrityViolationException e) {
			throw new AppException(ErrorCode.USER_DUPLICATE);
		}

		ProfileCreationRequest profileCreationRequest = ProfileCreationRequest.builder()
				.userId(entity.getId().toString())
				.displayedName(entity.getUsername())
				.build();

		profileClient.createProfile(profileCreationRequest);

		// Save & Return
		return userMapper.toResponse(entity);
	}

	public UserResponse socialRegister(SocialPlayerRequest request) {
		// Mapping socialRegisterRequest -> userEntity
		UserEntity entity = UserEntity.builder()
				.username(request.getUsername())
				.build();
		// Encode password
		entity.setPassword(passwordEncoder.encode(request.getPassword()));
		// Set default role
		if (entity.getRoleEntities().isEmpty())
			entity.setRoleEntities(Set.of(roleRepository.getReferenceByName(ROLE_PLAYER)));
		// Get & Set user email
		String email = redisService.getRegistrationToken(request.getRegistrationToken());
		entity.setEmail(email);
		// Add
		try {
			entity = userRepository.save(entity);
		} catch (DataIntegrityViolationException e) {
			throw new AppException(ErrorCode.USER_DUPLICATE);
		}

		ProfileCreationRequest profileCreationRequest = ProfileCreationRequest.builder()
				.userId(entity.getId().toString())
				.displayedName(entity.getUsername())
				.build();

		profileClient.createProfile(profileCreationRequest);

		// Save & Return
		return userMapper.toResponse(entity);
	}

	public UserResponse changePassword(ChangePasswordRequest request) {
		// Get context
		SecurityContext context = SecurityContextHolder.getContext();
		String myUsername = context.getAuthentication().getName();
		// Get old
		UserEntity foundEntity = userRepository.findByUsername(myUsername)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		// Old password unmatched exception
		if (!passwordEncoder.matches(request.getOldPassword(), foundEntity.getPassword()))
			throw new AppException(ErrorCode.WRONG_PASSWORD);
		// Change password
		foundEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
		// Save & Return
		return userMapper.toResponse(userRepository.save(foundEntity));
	}

	@PreAuthorize("hasRole('ADMIN') or (#id == authentication.principal.claims['uid'])")
	public String delete(String id) {
		// Fetch & Not found/deleted exception
		UserEntity entity =
				userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		// Delete
		userRepository.delete(entity);
		// Return ID
		return id;
	}
}
