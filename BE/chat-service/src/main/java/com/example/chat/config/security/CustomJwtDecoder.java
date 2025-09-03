package com.example.chat.config.security;

import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class CustomJwtDecoder implements JwtDecoder {
	@Override
	public Jwt decode(String accessToken) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(accessToken);

			return new Jwt(accessToken,
					signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
					signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
					signedJWT.getHeader().toJSONObject(),
					signedJWT.getJWTClaimsSet().getClaims()
			);
		} catch (ParseException e) {
			throw new JwtException("Invalid token");
		}
	}
}
