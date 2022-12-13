package threads;

import java.util.concurrent.Callable;

public abstract class DelayedTask<T> implements Callable<T> {

    private final T id;
    private final int delay;

    protected DelayedTask(T id) {
        this.id = id;
        this.delay = 1000;
    }

    protected DelayedTask(T id, int delay) {
        this.id = id;
        this.delay = delay;
    }

    @Override
    public T call() {

        System.out.printf("Thread %s - Task %s - START ...%n",
                Thread.currentThread().getName(), id);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            System.out.printf("Thread %s - Task %s - INTERRUPTED%n",
                    Thread.currentThread().getName(), id);
            return null;
        }

        System.out.printf("Thread %s - Task %s - END%n",
                Thread.currentThread().getName(), id);

        return id;
    }
}
