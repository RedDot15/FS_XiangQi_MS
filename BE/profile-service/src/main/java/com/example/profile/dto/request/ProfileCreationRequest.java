package com.example.profile.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
	@NotBlank(message = "User ID is required.")
	String userId;

	@NotBlank(message = "Displayed name is required.")
	@Size(min = 3, max = 20, message = "Displayed name must be between {min} and {max} characters.")
	String displayedName;
}
