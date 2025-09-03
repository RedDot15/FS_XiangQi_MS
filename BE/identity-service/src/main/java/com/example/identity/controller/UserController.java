package com.example.identity.controller;

import com.example.identity.dto.request.ChangePasswordRequest;
import com.example.identity.dto.request.UserRequest;
import com.example.identity.dto.request.SocialPlayerRequest;
import com.example.identity.helper.ResponseObject;
import com.example.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.identity.helper.ResponseBuilder.buildResponse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {
	UserService userService;

	@GetMapping("/me")
	public ResponseEntity<ResponseObject> getMyInfo() {
		// Fetch & Return all users
		return buildResponse(HttpStatus.OK, "My information fetch successfully.", userService.getMyInfo());
	}

	@PostMapping
	public ResponseEntity<ResponseObject> register(@Valid @RequestBody UserRequest userRequest) {
		// Create & Return user
		return buildResponse(HttpStatus.OK, "Created new user successfully.", userService.register(userRequest));
	}

	@PostMapping("/social")
	public ResponseEntity<ResponseObject> socialRegister(@Valid @RequestBody SocialPlayerRequest request) {
		// Create & Return user
		return buildResponse(HttpStatus.OK, "Created new user successfully.", userService.socialRegister(request));
	}

	@PutMapping("/me")
	public ResponseEntity<ResponseObject> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		// Update & Return user
		return buildResponse(HttpStatus.OK, "Changed password successfully.", userService.changePassword(request));
	}

	@DeleteMapping(value = "/{userId}")
	public ResponseEntity<ResponseObject> delete(@PathVariable String userId) {
		// Delete & Return id
		return buildResponse(HttpStatus.OK, "Deleted user successfully.", userService.delete(userId));
	}
}
