package com.example.demo.interceptor;

import com.example.demo.entity.TokenBucket;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    private static final Cache<String, TokenBucket> cache = Caffeine.newBuilder().build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return tryAcquire(request.getRemoteAddr(),1);
    }

    private synchronized boolean tryAcquire(String key, int permits) {
        TokenBucket tokenBucket = cache.getIfPresent(key);
        //如果没有创建令牌桶
        if (Objects.isNull(tokenBucket)) {
            tokenBucket = new TokenBucket(100L,2L);
            cache.put(key, tokenBucket);
            return true;
        }
        TokenBucket bucket = fill(tokenBucket);
        //如果令牌桶中令牌小于要取得令牌
        if (bucket.getStoredPermits() < permits) {
            return false;
        }
        bucket.setStoredPermits(tokenBucket.getStoredPermits() - permits);
        cache.put(key,bucket);
        return true;
    }

    private TokenBucket fill(TokenBucket tokenBucket) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > tokenBucket.getLastRefillTimestamp()) {
            long refill = (currentTimeMillis - tokenBucket.getLastRefillTimestamp()) /1000 * tokenBucket.getPermitsPerSecond();
            long storedPermits = Math.min(tokenBucket.getMaxPermits(), tokenBucket.getStoredPermits() + refill);
            tokenBucket.setStoredPermits(storedPermits);
            tokenBucket.setLastRefillTimestamp(currentTimeMillis);
        }
        return tokenBucket;
    }
}
