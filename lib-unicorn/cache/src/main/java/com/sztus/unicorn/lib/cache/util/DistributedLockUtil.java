package com.sztus.unicorn.lib.cache.util;

public class DistributedLockUtil {
//
//    /**
//     * 根据传入的资源名获取一把悲观锁，
//     *
//     * @param resourceKey 资源名
//     * @return 悲观锁 RLock
//     */
//    public static RLock lock(String resourceKey) {
//        RLock lock = redissonClient.getLock(resourceKey);
//        lock.lock();
//        return lock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把悲观锁，在设定的时间后自动释放锁
//     *
//     * @param resourceKey  资源名
//     * @param microseconds 超时自动释放锁 单位毫秒
//     * @return 悲观锁 RLock
//     */
//    public static RLock lock(String resourceKey, int microseconds) {
//        RLock lock = redissonClient.getLock(resourceKey);
//        lock.lock(microseconds, TimeUnit.MICROSECONDS);
//        return lock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把公平锁，
//     *
//     * @param resourceKey 资源名
//     * @return 公平锁 RLock
//     */
//    public static RLock fairLock(String resourceKey) {
//        RLock lock = redissonClient.getFairLock(resourceKey);
//        lock.lock();
//        return lock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把公平锁，在设定的时间后自动释放锁
//     *
//     * @param resourceKey  资源名
//     * @param microseconds 超时时间
//     * @return 公平锁 RLock
//     */
//    public static RLock fairLock(String resourceKey, int microseconds) {
//        RLock lock = redissonClient.getFairLock(resourceKey);
//        lock.lock(microseconds, TimeUnit.MICROSECONDS);
//        return lock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把自旋锁，
//     *
//     * @param resourceKey 资源名
//     * @return 自旋锁 RLock
//     */
//    public static RLock spinLock(String resourceKey) {
//        RLock lock = redissonClient.getSpinLock(resourceKey);
//        lock.lock();
//        return lock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把自旋锁，在设定的时间后自动释放锁
//     *
//     * @param resourceKey  资源名
//     * @param microseconds 超时时间
//     * @return 自旋锁 RLock
//     */
//    public static RLock spinLock(String resourceKey, int microseconds) {
//        RLock lock = redissonClient.getSpinLock(resourceKey);
//        lock.lock(microseconds, TimeUnit.MICROSECONDS);
//        return lock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把读锁，
//     *
//     * @param resourceKey 资源名
//     * @return 读锁 RLock
//     */
//    public static RLock readLock(String resourceKey) {
//        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(resourceKey);
//        RLock rLock = readWriteLock.readLock();
//        rLock.lock();
//        return rLock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把读锁，在设定的时间后自动释放锁
//     *
//     * @param resourceKey  资源名
//     * @param microseconds 超时时间
//     * @return 读锁 RLock
//     */
//    public static RLock readLock(String resourceKey, int microseconds) {
//        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(resourceKey);
//        RLock rLock = readWriteLock.readLock();
//        rLock.lock(microseconds, TimeUnit.MICROSECONDS);
//        return rLock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把写锁，
//     *
//     * @param resourceKey 资源名
//     * @return 写锁 RLock
//     */
//    public static RLock writeLock(String resourceKey) {
//        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(resourceKey);
//        RLock rLock = readWriteLock.writeLock();
//        rLock.lock();
//        return rLock;
//    }
//
//    /**
//     * 根据传入的资源名获取一把写锁，，在设定的时间后自动释放锁
//     *
//     * @param resourceKey  资源名
//     * @param microseconds 超时时间
//     * @return 写锁 RLock
//     */
//    public static RLock writeLock(String resourceKey, int microseconds) {
//        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(resourceKey);
//        RLock rLock = readWriteLock.writeLock();
//        rLock.lock(microseconds, TimeUnit.MICROSECONDS);
//        return rLock;
//    }
//
//    /**
//     * 释放锁
//     *
//     * @param lock 锁对象
//     */
//    public static void unlock(RLock lock) {
//        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//            lock.unlock();
//        }
//    }
//
//    /**
//     * 设置一个分布式闭锁,并设置 count 值
//     *
//     * @param resourceKey 资源名
//     * @param count count
//     * @return RCountDownLatch
//     */
//    public static RCountDownLatch setCountDownLatch(String resourceKey, Long count) {
//        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(resourceKey);
//        countDownLatch.trySetCount(count);
//        return countDownLatch;
//    }
//
//    /**
//     * 获取一个分布式闭锁
//     *
//     * @param resourceKey 资源名
//     * @return RCountDownLatch
//     */
//    public static RCountDownLatch getCountDownLatch(String resourceKey) {
//        return redissonClient.getCountDownLatch(resourceKey);
//    }
//
////    @Autowired
////    @Qualifier("primaryRedissonClient")
//    private void setDDObjectJdbcReader(RedissonClient RedissonClient) {
//        DistributedLockUtil.redissonClient = RedissonClient;
//    }
//
//    private static RedissonClient redissonClient;
}
