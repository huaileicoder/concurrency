package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TwinsLock implements Lock {
    private static Sync sync = new Sync(2);

    private static class Sync extends AbstractQueuedSynchronizer {
        Sync(int count) {
            if (count > 0) {
                setState(count);
            }
        }

        @Override
        protected int tryAcquireShared(int reduceCount) {
            for (; ; ) {
                int currentCount = getState();
                int newCount = currentCount - reduceCount;
                if (newCount < 0 || !compareAndSetState(currentCount, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int returnCount) {
            for (; ; ) {
                int currentCount = getState();
                int newCount = currentCount + returnCount;
                if (compareAndSetState(currentCount, newCount)) {
                    return true;
                }
            }
        }
    }

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
