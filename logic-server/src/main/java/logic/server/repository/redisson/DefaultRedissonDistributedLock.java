package logic.server.repository.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 简单的基于Redisson分布式锁接口实现类
 * <pre>
 *     使用Redisson实现
 * </pre>
 *
 * @author mark
 * @date 2023-04-17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultRedissonDistributedLock implements IDistributedLock{
    private final RedissonClient redissonClient;

    @Override
    public <T> T tryLockAndExecute(String key, long waitTime, long leaseTime, TimeUnit unit, IReturnHandle<T> action) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean tryLock = lock.tryLock(waitTime, leaseTime, unit);
            if (tryLock) {
                log.info("DefaultRedissonDistributedLock::tryLockAndExecute:key = {},waitTime = {},leaseTime = {},获取锁成功（IReturnHandle）", key,waitTime,leaseTime);
                return action.execute();
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

    @Override
    public void tryLockAndExecute(String key, long waitTime, long leaseTime, TimeUnit unit, IVoidHandle action) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean tryLock = lock.tryLock(waitTime, leaseTime, unit);
            if (tryLock) {
                log.info("DefaultRedissonDistributedLock::tryLockAndExecute:key = {},waitTime = {},leaseTime = {},获取锁成功（IVoidHandle）", key,waitTime,leaseTime);
                action.execute();
            } else {
                log.info("DefaultRedissonDistributedLock::tryLockAndExecute:key = {},waitTime = {},leaseTime = {},获取锁超时（IVoidHandle）", key,waitTime,leaseTime);
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public <T> T lockAndExecute(String key, long leaseTime, TimeUnit unit, IReturnHandle<T> action) {
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(leaseTime, unit);
            log.info("DefaultRedissonDistributedLock::lockAndExecute:key = {},leaseTime = {}, 获取锁成功（IReturnHandle）", key,leaseTime);
            return action.execute();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void lockAndExecute(String key, long leaseTime, TimeUnit unit, IVoidHandle action) {
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(leaseTime, unit);
            log.info("DefaultRedissonDistributedLock::lockAndExecute:key = {},leaseTime = {}, 获取锁成功（IVoidHandle）", key,leaseTime);
            action.execute();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public boolean unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        lock.unlock();
        return true;
    }
}
