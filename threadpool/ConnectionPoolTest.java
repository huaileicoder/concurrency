package concurrency.threadpool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolTest {
    private static ConnectionPool pool = new ConnectionPool(10);
    private static CountDownLatch start = new CountDownLatch(1);
    private static CountDownLatch end;

    public static void main(String[] args) throws Exception {
        int threadCount = 1;
        end = new CountDownLatch(threadCount);
        int count = 1000;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnectionRunner(count, got, notGot), "ConnectionRunnerThread");
            thread.start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke: " + (threadCount * count) );
        System.out.println("got connection: " + got);
        System.out.println("not got connection: " + notGot);
    }

    private static class ConnectionRunner implements Runnable {
        int count;
        AtomicInteger got;
        AtomicInteger notGot;

        ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
            }
            while (count > 0) {
                try {
                    Connection connection = pool.fecthConnection(10);
                    if (null != connection) {
                        try {
                            connection.createStatement();
                            connection.commit();
                        } finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    } else {
                        notGot.incrementAndGet();
                    }

                } catch (Exception e) {
                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }
}

