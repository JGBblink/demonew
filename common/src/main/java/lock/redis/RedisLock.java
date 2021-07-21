package lock.redis;


import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 使用redis实现的分布式锁. 为了不依赖redis, 这里需要调用者实现{@link RedisWrapper}. 如下:
 * <pre>{@code
 *     @Bean
 *     RedisLock.RedisWrapper redisWrapper(RedisTemplate redisTemplate) {
 *         return new RedisLock.RedisWrapper() {
 *             @Override
 *             public void delete(String key) {
 *                 redisTemplate.delete(key);
 *             }
 *
 *             @Override
 *             public boolean lock(String key, String value, long expireMills) {
 *                 return redisTemplate.opsForValue().setIfAbsent(key, value, expireMills, TimeUnit.MILLISECONDS);
 *             }
 *         };
 *     }
 * }</pre>
 * <p>
 * 然后使用{@link #tryAcquireRun(long, long, Supplier)} 来获取lock执行逻辑，明确指定 "等待锁时间" 和 "锁的超时时间".
 */
@Slf4j
public class RedisLock {

    public static final String DEFAULT_LOCK_SUFFIX = ":lock";
    private RedisWrapper redis;

    /**
     * 默认超时时间（毫秒）
     */
    private static final long DEFAULT_TIME_OUT_MILLIS = 5 * 1000;
    private static final Random RANDOM = new Random();
    /**
     * 锁的超时时间（豪秒），过期删除
     */
    public static final int EXPIRE_IN_MILLIS = 1 * 60 * 1000;

    private String key;
    // 锁状态标志
    private boolean locked = false;

    public interface RedisWrapper {
        /**
         * 删除key
         *
         * @param key
         */
        void delete(String key);

        /**
         * 锁定key. 通常使用SetIfAbsent();
         *
         * @param key
         * @param value
         * @param expireMills
         * @return
         */
        boolean lock(String key, String value, long expireMills);
    }

    /**
     * 关闭锁，该方法不建议外部直接使用，<br>
     * 对于加锁执行的操作，建议直接使用 {@link RedisLock#tryAcquireRun(long, long, java.util.function.Supplier)}，会自动执行close操作。
     */
    private void close() {
        if (this.locked) {
            this.redis.delete(this.key);
        }
    }

    /**
     * This creates a RedisLock
     *
     * @param key   key
     * @param redis 数据源
     */
    public RedisLock(String key, RedisWrapper redis) {
        this(key, redis, DEFAULT_LOCK_SUFFIX);
    }

    /**
     * This creates a RedisLock
     *
     * @param key   key
     * @param redis 数据源
     */
    public RedisLock(String key, RedisWrapper redis, String suffix) {
        this.key = key + Optional.ofNullable(suffix).orElse(DEFAULT_LOCK_SUFFIX);
        this.redis = redis;
    }

    /**
     * 尝试在timeoutMillis毫秒内获取锁并设置锁的过期时间为expireMillis毫秒，若获取锁成功，则执行supplier的逻辑，并返回supplier执行结果。然后关闭锁<br>
     * <pre>
     * 锁的释放，由2方面保证：
     * 1、supplier方法执行完成后，会主动释放锁。
     * 2、设置锁的过期时间
     * </pre>
     * 如果只是单纯的尝试获取锁并执行，无需等待锁，可以<b>将timeoutMillis参数设置为0。</b>
     *
     * @param timeoutMillis 等待获取锁的时间 单位毫秒（会在等待时间内不停自旋尝试获取锁。）如果超过该时间还没成功获取到锁，则抛出获取锁失败的BizException
     *                      <b>timeoutMillis=0，则表示只进行一次获取锁的尝试。获取失败，直接抛获取锁失败的异常</b>
     * @param expireMillis  锁的过期时间，保证锁最长的持有时间。（如果主动释放锁失败，会有该参数保证锁成功释放）
     * @param supplier      需要执行的方法
     * @param <T>           返回参数类型
     * @return
     */
    public <T> T tryAcquireRun(final long timeoutMillis, final long expireMillis, Supplier<T> supplier) {
        if (!lock(timeoutMillis, expireMillis)) {
            throw new AcquireLockFailException("获取锁失败 " + key);
        }
        try {
            return supplier.get();
        } finally {
            close();
        }
    }

    /**
     * 尝试获取锁，并执行supplier.get()方法，返回结果。<br>
     * 该方法使用了默认的锁等待时间和过期时间：<br>
     * 等待锁时间={@link #DEFAULT_TIME_OUT_MILLIS 5秒}<br>
     * 锁过期时间={@link #EXPIRE_IN_MILLIS 1分钟}<br>
     * 调用该方法，效果等同于 {@link #tryAcquireRun(long, long, Supplier)}
     * -> tryAcquireRun(DEFAULT_TIME_OUT_MILLIS, EXPIRE_IN_MILLIS, supplier);
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public <T> T tryAcquireRun(Supplier<T> supplier) {
        if (!lock()) {
            throw new AcquireLockFailException("获取锁失败 " + key);
        }
        try {
            return supplier.get();
        } finally {
            close();
        }
    }

    /**
     * 尝试获取锁，并执行supplier.get()方法，返回结果。<br>
     * 该方法使用了默认的锁过期时间：<br>
     * 锁过期时间={@link #EXPIRE_IN_MILLIS 1分钟}<br>
     * 调用该方法，效果等同于 {@link #tryAcquireRun(long, long, Supplier)}
     * -> tryAcquireRun(timeoutMillis, EXPIRE_IN_MILLIS, supplier);
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public <T> T tryAcquireRun(long timeoutMillis, Supplier<T> supplier) {
        if (!lock(timeoutMillis)) {
            throw new AcquireLockFailException("获取锁失败 " + key);
        }
        try {
            return supplier.get();
        } finally {
            close();
        }
    }

    /**
     * 加锁 应该以： lock(); try { doSomething(); } finally { close()； } 的方式调用<br>
     * 外部不建议直接使用该方法，建议使用{@link #tryAcquireRun(long, long, Supplier)}明确指定锁的等待和过期时间
     *
     * @param timeoutMillis 超时时间(毫秒)
     * @return 成功或失败标志
     */
    private boolean lock(long timeoutMillis) {
        return lock(timeoutMillis, EXPIRE_IN_MILLIS);
    }

    /**
     * 加锁 应该以： lock(); try { doSomething(); } finally { close()； } 的方式调用<br>
     * 外部不建议直接使用该方法，建议使用{@link #tryAcquireRun(long, long, Supplier)}明确指定锁的等待和过期时间
     *
     * @param timeoutMillis 超时时间(毫秒
     * @param expireMillis  锁的超时时间（毫秒），过期删除
     * @return 成功或失败标志
     */
    private boolean lock(final long timeoutMillis, final long expireMillis) {
        long nano = System.nanoTime();
        long timeoutNano = TimeUnit.MILLISECONDS.toNanos(timeoutMillis);
        try {
            do {
                boolean ok = redis.lock(key, "true", expireMillis);
                if (ok) {
                    this.locked = true;
                    return this.locked;
                }
                // 短暂休眠，避免出现活锁
                Thread.sleep(3, RANDOM.nextInt(500));
            } while ((System.nanoTime() - nano) < timeoutNano);
        } catch (Exception e) {
            throw new AcquireLockFailException("Locking error", e);
        }
        return false;
    }

    /**
     * 加锁 应该以： lock(); try { doSomething(); } finally { close()； } 的方式调用<br>
     * 外部不建议直接使用该方法，建议使用{@link #tryAcquireRun(long, long, Supplier)}明确指定锁的等待和过期时间
     *
     * @return 成功或失败标志
     */
    private boolean lock() {
        return lock(DEFAULT_TIME_OUT_MILLIS);
    }

    /** 当获取锁失败的时候抛出该异常，方便调用方捕获处理 */
    public static class AcquireLockFailException extends RuntimeException {
        public AcquireLockFailException() {
            super();
        }

        public AcquireLockFailException(String msg) {
            super(msg);
        }

        public AcquireLockFailException(Throwable throwable) {
            super(throwable);
        }

        public AcquireLockFailException(String msg, Throwable throwable) {
            super(msg, throwable);
        }
    }

}
