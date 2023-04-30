package logic.server.repository.redisson;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁基础接口
 *
 * @author mark
 * @date 2023-04-17
 */
public interface IDistributedLock {
    /**
     * 获取分布式锁，并且执行用户传入的操作
     * 在指定时间内未得到锁，则直接返回结果，不会阻塞
     * 用户需要自行处理异常
     * <pre>
     *  waitTime: 等待时间。当该值为0的时候，即不等待，无论是否获取到锁，都直接返回结果
     *  leaseTime: 释放时间。当该值为-1的时候，则释放时间由“显式unLock调用”决定。大于-1，则表示，释放时间由leaseTime决定
     * </>
     * @param key   锁的键值
     * @param waitTime  等待锁的时间
     * @param leaseTime     最小释放锁的时间
     * @param unit  时间单位
     * @param action    执行的动作，带返回值
     */
    <M> M tryLockAndExecute(String key, long waitTime, long leaseTime, TimeUnit unit, IReturnHandle<M> action) throws InterruptedException;

    /**
     * 获取分布式锁，并且执行用户传入的操作
     * 在指定时间内未得到锁，则直接返回结果，不会阻塞
     * 用户需要自行处理异常
     * <pre>
     *  waitTime: 等待时间。当该值为0的时候，即不等待，无论是否获取到锁，都直接返回结果
     *  leaseTime: 释放时间。当该值为-1的时候，则释放时间由“显式unLock调用”决定。大于-1，则表示，释放时间由leaseTime决定
     * </>
     * @param key   锁的键值
     * @param waitTime  等待锁的时间
     * @param leaseTime     最小释放锁的时间
     * @param unit  时间单位
     * @param action    执行的动作，不带返回值
     */
    <M> void tryLockAndExecute(String key, long waitTime, long leaseTime, TimeUnit unit, IVoidHandle action) throws InterruptedException;

    /**
     * 阻塞等待获取分布式锁，并且执行用户传入的操作
     * <pre>
     *  leaseTime: 释放时间。当该值为-1的时候，则释放时间由“显式unLock调用”决定。大于-1，则表示，释放时间由leaseTime决定
     * </>
     * @param key   锁的键值
     * @param leaseTime     最小释放锁的时间
     * @param unit  时间单位
     * @param action    执行的动作，带返回值
     */
    <M> M lockAndExecute(String key, long leaseTime, TimeUnit unit, IReturnHandle<M> action);

    /**
     * 阻塞等待获取分布式锁，并且执行用户传入的操作
     * <pre>
     *  leaseTime: 释放时间。当该值为-1的时候，则释放时间由“显式unLock调用”决定。大于-1，则表示，释放时间由leaseTime决定
     * </>
     * @param key   锁的键值
     * @param leaseTime     最小释放锁的时间
     * @param unit  时间单位
     * @param action    执行的动作，带返回值
     */
    void lockAndExecute(String key, long leaseTime, TimeUnit unit, IVoidHandle action);

    boolean unlock(String key);
}
