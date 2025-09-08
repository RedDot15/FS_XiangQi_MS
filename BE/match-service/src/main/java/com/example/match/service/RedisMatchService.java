package com.example.match.service;

import com.example.match.entity.redis.MatchStateEntity;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
@Service
public class RedisMatchService {
    RedisTemplate<String, MatchStateEntity> msRedisTemplate;
    RedisTemplate<String, Long> longRedisTemplate;

    private static final String MATCH_STATE_KEY = "match:%s:state:";
    private static final String MATCH_STATE_EXPIRATION_KEY = "match:%s:expiration:";

    // Save
    public void saveMatchState(String matchId, MatchStateEntity entity) {
        msRedisTemplate.opsForValue().set(String.format(MATCH_STATE_KEY, matchId), entity);
    }

    public void saveMatchExpiration(String matchId, Long expirationTime) {
        longRedisTemplate.opsForValue().set(String.format(MATCH_STATE_EXPIRATION_KEY, matchId), expirationTime, expirationTime, TimeUnit.MILLISECONDS);
    }

    // Get
    public MatchStateEntity getMatchState(String matchId) {
        return msRedisTemplate.opsForValue().get(String.format(MATCH_STATE_KEY, matchId));
    }

    // Delete
    public void deleteMatchState(String matchId) {
        msRedisTemplate.delete(String.format(MATCH_STATE_KEY, matchId));
    }

    public void deleteMatchExpiration(String matchId) {
        msRedisTemplate.delete(String.format(MATCH_STATE_EXPIRATION_KEY, matchId));
    }
}
