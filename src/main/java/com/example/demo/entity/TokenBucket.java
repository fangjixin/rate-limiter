package com.example.demo.entity;

public class TokenBucket {

    /**
     * 当前可用令牌数
     */
    private Long storedPermits;

    /**
     * 最大令牌数
     */
    private Long maxPermits;

    /**
     * 令牌发放速率
     */
    private Long permitsPerSecond;

    /**
     * 下次请求可以获取令牌的起始时间，默认当前系统时间
     */
    private Long lastRefillTimestamp;

    public TokenBucket(Long maxPermits, Long permitsPerSecond) {
        this.storedPermits = 0L;
        this.maxPermits = maxPermits;
        this.permitsPerSecond = permitsPerSecond;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }


    public Long getStoredPermits() {
        return storedPermits;
    }

    public void setStoredPermits(Long storedPermits) {
        this.storedPermits = storedPermits;
    }

    public Long getMaxPermits() {
        return maxPermits;
    }

    public void setMaxPermits(Long maxPermits) {
        this.maxPermits = maxPermits;
    }

    public Long getPermitsPerSecond() {
        return permitsPerSecond;
    }

    public void setPermitsPerSecond(Long permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
    }

    public Long getLastRefillTimestamp() {
        return lastRefillTimestamp;
    }

    public void setLastRefillTimestamp(Long lastRefillTimestamp) {
        this.lastRefillTimestamp = lastRefillTimestamp;
    }
}
