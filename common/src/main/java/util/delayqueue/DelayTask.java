package util.delayqueue;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class DelayTask<T> implements Delayed {
    private T id;
    private Map extend = new HashMap();

    private final long start = System.nanoTime();
    private long time;

    /**
     * delay handle task
     *
     * @param id
     * @param time : milliseconds
     */
    public DelayTask(T id, long time) {
        this.id = id;
        this.time = TimeUnit.NANOSECONDS.convert(time, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert((start + time) - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long d = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public <K, V> void putExt(K k, V v) {
        extend.put(k, v);
    }

    public <K, V> V getExt(K k, Class<V> clazz) {
        if (extend.containsKey(k)) {
            return (V) extend.get(k);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DelayTask)) return false;
        DelayTask<?> that = (DelayTask<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
