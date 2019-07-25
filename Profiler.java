package concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class Profiler {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Runner(), "LocalThread");
            thread.start();
        }
    }

    private static class Runner implements Runnable {
        private static final AtomicInteger i = new AtomicInteger();

        private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>() {
            @Override
            protected Long initialValue() {
                return System.currentTimeMillis();
            }
        };

        private static void begin() {
            TIME_THREADLOCAL.set(System.currentTimeMillis());
        }

        private static long end() {
            return System.currentTimeMillis() - TIME_THREADLOCAL.get();
        }

        @Override
        public void run() {
            begin();
            SleepUtils.second(i.getAndIncrement());
            System.out.println("cost : " + end() + " mills");
        }
    }
}
