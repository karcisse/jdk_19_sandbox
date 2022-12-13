import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;
import threads.DelayedTask;
import threads.SqrtTask;
import threads.SquareTask;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class VirtualThreadsTest {

    private List<? extends DelayedTask<Integer>> tasks;

    @Before
    public void setUpTasks() {
        this.tasks = IntStream.rangeClosed(1, 1000)
                .boxed()
                .map(i -> i % 2 == 1 ? new SquareTask(i) : new SqrtTask(i))
                .toList();
    }

    @Test
    public void oldFashionThreads() {
        System.out.println("Starting old fashion threads!");
        long time = executeTasks(this.tasks, Executors.newFixedThreadPool(100));
        System.out.printf("Old fashion threads took %dms%n", time);
    }

    @Test
    public void virtualThreads() {
        System.out.println("Starting virtual threads");
        long time = executeTasks(this.tasks, Executors.newVirtualThreadPerTaskExecutor());
        System.out.printf("Virtual threads took %dms%n", time);
    }


    //region Reactive Programming
    @Test
    public void reactiveTasks() {
        System.out.println("Starting reactive programming");
        long startTimestamp = System.currentTimeMillis();

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
        System.out.printf("Reactive programming took %dms%n", time);
    }
    //endregion

    //region Starting single Virtual Thread
    @Test
    public void otherWaysToStartThreads() {
        Runnable task = () -> this.tasks.get(0).call();
        Thread.startVirtualThread(task);

        Thread.ofVirtual()
                .name("I AM VIRTUAL TASK!")
                .start(task);
        Thread.ofPlatform()
                .name("I AM PLATFORM TASK!")
                .start(task);
    }

    @Test
    public void createTasksWithBuilder() {
        List<Thread> squareTasks = tasks.stream()
                .filter(t -> t instanceof SquareTask)
                .map(t -> Thread.ofVirtual()
                        .name("VIRTUAL - " + ((SquareTask) t).getClass().getSimpleName())
                        .unstarted(t::call))
                .toList();

        squareTasks.forEach(Thread::start);
    }
    //endregion

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
