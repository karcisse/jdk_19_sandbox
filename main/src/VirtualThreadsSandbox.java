import threads.SquareTask;

import java.util.Collection;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class VirtualThreadsSandbox implements Sandbox {
    @Override
    public void play() {
        Collection<SquareTask> tasks = IntStream.rangeClosed(1, 1000)
                .boxed()
                .map(SquareTask::new).toList();
        long oldFashionTime = oldFashionThreads(tasks);
        long virtualTime = virtualThreads(tasks);

        System.out.printf("Old fashioned threads -> %dms%nVirtual threads -> %dms%n", oldFashionTime, virtualTime);
    }

    private <T extends Callable<Integer>> long oldFashionThreads(Collection<T> tasks) {
        return executeTasks(tasks, Executors.newFixedThreadPool(100));
    }

    private <T extends Callable<Integer>> long virtualThreads(Collection<T> tasks) {
        return executeTasks(tasks, Executors.newVirtualThreadPerTaskExecutor());
    }

    private <T extends Callable<Integer>> long executeTasks(Collection<T> tasks, ExecutorService executor) {
        long startTimestamp = System.currentTimeMillis();

        Collection<Future<Integer>> futures;
        try {
             futures = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.err.printf("Error while running threads%n");
            throw new RuntimeException(e);
        }

        Integer sum = futures.stream().map(integerFuture -> {
            try {
                return integerFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).mapToInt(Integer::intValue).sum();

        long time = System.currentTimeMillis() - startTimestamp;
        System.out.printf("Time = %dms - Sum = %d%n", time, sum);

        executor.shutdown();

        return time;
    }
}
