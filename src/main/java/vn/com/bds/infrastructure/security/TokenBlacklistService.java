package vn.com.bds.infrastructure.security; // Or a sub-package like infrastructure.cache

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Optional for logging
import org.springframework.data.redis.core.StringRedisTemplate; // For Redis interaction
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service // Register as a Spring Bean
@RequiredArgsConstructor
@Slf4j // Optional for logging
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate; // Inject Redis client
    private static final String BLACKLIST_PREFIX = "blacklist:jwt:"; // Prefix keys in Redis

    /**
     * Adds a token to the blacklist with a specific expiration time.
     * @param token The JWT string to blacklist.
     * @param expiryDuration The remaining validity duration of the token.
     */
    public void addToBlacklist(String token, Duration expiryDuration) {
        if (token == null || expiryDuration == null || expiryDuration.isNegative() || expiryDuration.isZero()) {
            log.warn("Attempted to blacklist token with invalid parameters.");
            return;
        }
        try {
            // Store the token (or its unique ID like JTI if preferred) as the key
            // Set a simple value (like "1") and the calculated expiration
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "1", expiryDuration);
            log.debug("Token added to blacklist with expiry: {}", expiryDuration);
        } catch (Exception e) {
            log.error("Failed to add token to Redis blacklist", e);
            // Handle Redis connection errors if necessary
        }
    }

    /**
     * Checks if a token exists in the blacklist.
     * @param token The JWT string to check.
     * @return true if the token is blacklisted, false otherwise.
     */
    public boolean isBlacklisted(String token) {
        if (token == null) {
            return false;
        }
        try {
            // Check if the key exists in Redis
            return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
        } catch (Exception e) {
            log.error("Failed to check token in Redis blacklist", e);
            // Decide how to handle Redis errors (fail-safe: assume not blacklisted?)
            return false;
        }
    }
}