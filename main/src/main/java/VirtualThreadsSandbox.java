import rx.Observable;
import rx.schedulers.Schedulers;
import threads.SquareTask;

import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class VirtualThreadsSandbox implements Sandbox {
    @Override
    public void play() {
        Collection<SquareTask> tasks = IntStream.rangeClosed(1, 1000)
                .boxed()
                .map(SquareTask::new).toList();
//        long time = 0;
        long time = oldFashionThreads(tasks);
//        long time = virtualThreads(tasks);
//        long time = reactiveTasks(tasks);

//        createTasksWithBuilder(tasks);
//        singleVirtualThread(() -> new SquareTask(12).call());

        System.out.printf("TIME -> %dms%n", time);
    }

    private void singleVirtualThread(Runnable task) {
//        Thread.startVirtualThread(task);

        Thread.ofVirtual()
                .name("I AM VIRTUAL TASK!")
                .start(task);
        Thread.ofPlatform()
                .name("I AM PLATFORM TASK!")
                .start(task);
    }

    private <T extends Callable<Integer>> void createTasksWithBuilder(Collection<T> tasks) {
        tasks.forEach(t -> Thread.ofVirtual()
                        .start(() -> {
                            try {
                                t.call();
                            } catch (Exception e) {
                                // ignore
                            }
                        }));

    }

    private <T extends Callable<Integer>> long oldFashionThreads(Collection<T> tasks) {
        return executeTasks(tasks, Executors.newFixedThreadPool(100));
    }

    private <T extends Callable<Integer>> long virtualThreads(Collection<T> tasks) {
        return executeTasks(tasks, Executors.newVirtualThreadPerTaskExecutor());
    }

    private <T extends Callable<Integer>> long reactiveTasks(Collection<T> tasks) {
        long startTimestamp = System.currentTimeMillis();

        long sum = 0;
        AtomicLong end = new AtomicLong();

        Observable.from(tasks)
                .flatMap(task -> Observable.fromCallable(task)
                        .subscribeOn(Schedulers.io())
                )
                .subscribe(
                        onNext -> { },
                        onNext -> { },
                        () -> {
                            System.out.printf("Real time -> %d%n", System.currentTimeMillis() - startTimestamp);
                            end.set(System.currentTimeMillis());
                        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long time = end.get() - startTimestamp;
        System.out.printf("Time = %dms - Sum = %d%n", time, sum);

        return time;
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
