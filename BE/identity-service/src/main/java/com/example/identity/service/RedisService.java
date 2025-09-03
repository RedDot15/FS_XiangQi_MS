package com.example.identity.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
@Service
public class RedisService {
    RedisTemplate<String, Long> redisLongTemplate;
    RedisTemplate<String, String> redisStringTemplate;

    // Save
    public void saveInvalidatedToken(String invalidatedToken, Long timeExpiration) {
        redisLongTemplate.opsForValue().set(invalidatedToken, timeExpiration, timeExpiration, TimeUnit.MILLISECONDS);
    }

    public void saveRegistrationToken(String registrationToken, String email, Long timeExpiration) {
        redisStringTemplate.opsForValue().set(registrationToken, email, timeExpiration, TimeUnit.MILLISECONDS);
    }

    // Get
    public Long getInvalidatedToken(String invalidatedToken) {
        return redisLongTemplate.opsForValue().get(invalidatedToken);
    }

    public String getRegistrationToken(String registrationToken) {
        return Objects.requireNonNull(redisStringTemplate.opsForValue().get(registrationToken));
    }
}
