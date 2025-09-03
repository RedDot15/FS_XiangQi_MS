package com.example.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
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
