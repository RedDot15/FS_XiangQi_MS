package com.example.identity.controller;

import com.example.identity.dto.request.AuthenticationRequest;
import com.example.identity.dto.request.OutboundAuthenticationRequest;
import com.example.identity.dto.request.RefreshRequest;
import com.example.identity.helper.ResponseObject;
import com.example.identity.service.AuthenticationService;
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
@RequestMapping("/auth")
public class AuthenticationController {
	AuthenticationService authenticationService;

	@PostMapping("/outbound/{provider}/authenticate")
	public ResponseEntity<ResponseObject> outboundAuthenticate(@PathVariable String provider, @RequestBody OutboundAuthenticationRequest request){
		return buildResponse(HttpStatus.OK, "Outbound authenticate successfully.", authenticationService.outboundAuthenticate(request));
	}

	@PostMapping(value = "/tokens")
	public ResponseEntity<ResponseObject> authenticate(@Valid @RequestBody AuthenticationRequest request) {
		return buildResponse(HttpStatus.OK, "Authenticate successfully.", authenticationService.authenticate(request));
	}

	@PostMapping("/tokens/refresh")
	public ResponseEntity<ResponseObject> refreshToken(@Valid @RequestBody RefreshRequest request) {
		return buildResponse(HttpStatus.OK, "Refresh token successfully.", authenticationService.refresh(request));
	}

	@PostMapping("/tokens/introspect")
	public ResponseEntity<ResponseObject> introspect(@RequestHeader("Authorization") String token) {
		authenticationService.introspect(token);
		return buildResponse(HttpStatus.OK, "Token valid.", null);
	}

	@DeleteMapping("/tokens")
	public ResponseEntity<ResponseObject> logout() {
		authenticationService.logout();
		return buildResponse(HttpStatus.OK, "Log out successfully.", null);
	}
}
