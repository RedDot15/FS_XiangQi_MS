package com.example.profile.controller;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.helper.ResponseObject;
import com.example.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.profile.helper.ResponseBuilder.buildResponse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class ProfileController {
	ProfileService profileService;

	@GetMapping("/profiles")
	public ResponseEntity<ResponseObject> getAll(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		// Fetch & Return all users
		return buildResponse(HttpStatus.OK, "Get all profile successfully.", profileService.getAll(page, size));
	}

	@GetMapping("/profiles/me")
	public ResponseEntity<ResponseObject> getMyInfo() {
		// Fetch & Return all users
		return buildResponse(HttpStatus.OK, "My profile fetch successfully.", profileService.getMyInfo());
	}

	@GetMapping("/profiles/{userId}")
	public ResponseEntity<ResponseObject> getByUserId(@PathVariable String userId) {
		// Fetch & Return all users
		return buildResponse(HttpStatus.OK, "profile fetch successfully.", profileService.getByUserId(userId));
	}

	@GetMapping("/internal/profiles/{userId}/rating")
	public ResponseEntity<ResponseObject> getRatingByUserId(@PathVariable String userId) {
		return buildResponse(HttpStatus.OK, "Get rating successfully.", profileService.getRatingByUserId(userId));
	}

	@PostMapping("/internal/profiles")
	public ResponseEntity<ResponseObject> create(@Valid @RequestBody ProfileCreationRequest request) {
		// Create & Return user
		return buildResponse(HttpStatus.OK, "Created new profile successfully.", profileService.create(request));
	}

	@PostMapping("/internal/profiles/{userId}/rating")
	public ResponseEntity<ResponseObject> updateRating(@PathVariable String userId, @RequestBody Integer changedRating) {
        profileService.updateRating(userId, changedRating);
		// Create & Return user
		return buildResponse(HttpStatus.OK, "Update rating successfully.", null);
	}

	@DeleteMapping(value = "/profiles/{userId}")
	public ResponseEntity<ResponseObject> delete(@PathVariable String userId) {
		// Delete & Return id
		return buildResponse(HttpStatus.OK, "Deleted profile successfully.", profileService.delete(userId));
	}
}
