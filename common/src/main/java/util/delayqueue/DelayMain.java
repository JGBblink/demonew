package util.delayqueue;

public class DelayMain {

    private final static DemoDelayQueue<Long> queue = new DemoDelayQueue<>(DelayMain::test);

    private static void test(DelayTask<Long> task) {
        System.out.println(task.getId());
    }

    public static void main(String[] args) throws Exception{
        queue.pull(new DelayTask<>(1L,1000));
        queue.pull(new DelayTask<>(2L,1050));
        queue.pull(new DelayTask<>(3L,1000),true);
        queue.pull(new DelayTask<>(3L,3000),true);
        queue.pull(new DelayTask<>(3L,4000));

        Thread.sleep(5000);
    }
}
