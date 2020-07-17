package concurrency;

public class Monitor {
    private int a;

    public static void main(String[] args) {
    }

    private class AddRunner implements Runnable {
        @Override
        public void run() {
            writer();
        }

    }

    private synchronized void writer() {
        a++;
    }

    private class ReadRunner implements Runnable {
        @Override
        public void run() {
            System.out.println(reader());
        }

    }

    private int reader() {
        return a;
    }
}
