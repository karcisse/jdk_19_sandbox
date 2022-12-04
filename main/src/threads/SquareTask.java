package threads;

import java.util.concurrent.Callable;

public class SquareTask implements Callable<Integer> {

    private final int id;

    public SquareTask(int id) {
        this.id = id;
    }

    @Override
    public Integer call() {
        System.out.printf("Thread %s - Task %d - START ...%n",
                Thread.currentThread().getName(), id);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.printf("Thread %s - Task %d - ERROR%n",
                    Thread.currentThread().getName(), id);
            return -1;
        }

        System.out.printf("Thread %s - Task %d - END%n",
                Thread.currentThread().getName(), id);

        return id * id;
    }
}
