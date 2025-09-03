package com.example.profile.service;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.response.ProfileResponse;
import com.example.profile.entity.ProfileEntity;
import com.example.profile.exception.AppException;
import com.example.profile.exception.ErrorCode;
import com.example.profile.mapper.ProfileMapper;
import com.example.profile.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Service
public class ProfileService {
	ProfileRepository profileRepository;
	ProfileMapper profileMapper;

	public static final Integer DEFAULT_RATING = 1200;

	public List<ProfileResponse> getAll(int page, int size) {
		// Define pageable
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "rating"));
		// Return player list
		return profileRepository.findAll(pageable)
				.stream().map(profileMapper::toResponse)
				.collect(Collectors.toList());
	}

	public ProfileResponse getMyInfo() {
		// Get Jwt token from Context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) authentication.getPrincipal();
		// Get token information
		String uid = jwt.getClaim("uid");
		// Fetch
		ProfileEntity entity = profileRepository
				.findByUserId(uid)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		// Return
		return profileMapper.toResponse(entity);
	}

	public ProfileResponse getByUserId(String userId) {
		// Fetch
		ProfileEntity entity = profileRepository
				.findByUserId(userId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		// Return
		return profileMapper.toResponse(entity);
	}

	public Integer getRatingByUserId(String userId) {
		return profileRepository.findRatingByUserId(userId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
	}

	public ProfileResponse create(ProfileCreationRequest profileCreationRequest) {
		// Mapping userRequest -> userEntity
		ProfileEntity entity = profileMapper.toEntity(profileCreationRequest);
		// Set default rating
		entity.setRating(DEFAULT_RATING);
		// Save & Return
		return profileMapper.toResponse(profileRepository.save(entity));
	}

    public void updateRating(String userId, Integer changedRating) {
        // Get
        ProfileEntity entity = profileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // Update rating
        entity.setRating(entity.getRating() + changedRating);
    }

	@PreAuthorize("hasAuthority('DELETE_PROFILE') or (#userId == authentication.principal.claims['uid'])")
	public String delete(String userId) {
		// Fetch & Not found/deleted exception
		ProfileEntity entity = profileRepository.findById(userId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		// Delete
		profileRepository.delete(entity);
		// Return ID
		return userId;
	}
}
