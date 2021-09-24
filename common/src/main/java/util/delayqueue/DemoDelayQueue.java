package util.delayqueue;

import cn.hutool.core.util.BooleanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.function.Consumer;

public class DemoDelayQueue<T> {
    private static final Logger log = LoggerFactory.getLogger(DemoDelayQueue.class);
    private final DelayQueue<DelayTask<T>> delayQueue = new DelayQueue<>();
    private volatile Boolean NEW_THREAD = false;
    private volatile Boolean SHUTDOWN = false;
    private Consumer<DelayTask<T>> consumer;
    private String name = "transfer delayQueue";

    private DemoDelayQueue() {
    }

    public DemoDelayQueue(Consumer<DelayTask<T>> consumer) {
        this(null, consumer);
    }

    public DemoDelayQueue(String name, Consumer<DelayTask<T>> consumer) {
        this.consumer = consumer;
        if (Objects.nonNull(name)) {
            this.name = name;
        }
        run();
    }

    public void pull(DelayTask<T> task) {
        pull(task, false);
    }

    public void pull(DelayTask<T> task, Boolean isReplace) {
        if (Objects.isNull(task) || delayQueue.contains(task)) {
            if (isReplace) {
                log.info("replace task id={}", task.getId());
                delayQueue.remove(task);
            } else {
                log.info("skip task id={}", task.getId());
                return;
            }
        }
        delayQueue.put(task);
    }

    public void destroy() {
        SHUTDOWN = true;
    }

    private void run() {
        // delay execute task for only new Thread
        if (BooleanUtil.isFalse(NEW_THREAD)) {
            synchronized (NEW_THREAD) {
                if (BooleanUtil.isFalse(NEW_THREAD)) {
                    NEW_THREAD = true;
                    CompletableFuture.runAsync(() -> {
                        log.info(name + " is started");
                        while (canTake()) {
                            take();
                        }
                        log.info(name + " is shutdown...");
                    });
                }
            }
        }
    }

    private boolean canTake() {
        return !SHUTDOWN || !delayQueue.isEmpty();
    }

    private void take() {
        try {
            final DelayTask<T> take = delayQueue.take();
            if (Objects.nonNull(take) && Objects.nonNull(consumer)) {
                CompletableFuture.runAsync(() -> consumer.accept(take));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
