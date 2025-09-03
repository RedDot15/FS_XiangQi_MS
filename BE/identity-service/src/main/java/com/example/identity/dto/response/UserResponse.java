package com.example.identity.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse implements Comparable {
	String id;

	String username;

	@Override
	public int compareTo(Object o) {
		return id.compareTo(((UserResponse) o).getId());
	}
}
