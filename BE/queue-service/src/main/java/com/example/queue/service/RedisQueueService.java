package com.example.queue.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class RedisQueueService {
    RedisTemplate<String, String> redisTemplate;

    // Key
    private static final String QUEUE_KEY = "queue:";

    // Lock key
    private static final String QUEUE_LOCK_KEY = "lock:queue:";

    // Time
    private static final long LOCK_TIMEOUT_SECONDS = 10;
    private static final long RETRY_DELAY_MILLIS = 100;

    // Push
    public void rightPush(String id) {
        redisTemplate.opsForList().rightPush(QUEUE_KEY, id);
    }

    // Get
    public List<String> getAll() {
        return Objects.requireNonNull(redisTemplate.opsForList().range(QUEUE_KEY, 0, -1));
    }

    public Long getQueueSize() {
        return Objects.requireNonNull(redisTemplate.opsForList().size(QUEUE_KEY));
    }

    public String getPlayerIdByIndex(int idx) {
        return Objects.requireNonNull(redisTemplate.opsForList().index(QUEUE_KEY, idx));
    }

    // Delete
    public void deletePlayerId(String id) {
        Objects.requireNonNull(redisTemplate.opsForList().remove(QUEUE_KEY, 1, id));
    }

    // Acquire lock
    public void acquireQueueLock() {
        while (true) {
            // Try to set the lock with a timeout
            Boolean success = redisTemplate.opsForValue().setIfAbsent(QUEUE_LOCK_KEY, "", LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (success != null && success) {
                return;
            }
            try {
                Thread.sleep(RETRY_DELAY_MILLIS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while waiting to retry lock acquisition", e);
            }
        }
    }

    // Release lock
    public void releaseQueueLock() {
        // Remove the lock
        redisTemplate.delete(QUEUE_LOCK_KEY);
    }
}
