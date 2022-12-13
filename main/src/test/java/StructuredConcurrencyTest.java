import jdk.incubator.concurrent.StructuredTaskScope;
import org.junit.Test;
import threads.FailingTask;
import threads.PrettyTextTask;
import threads.SqrtTask;
import threads.SquareTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StructuredConcurrencyTest {

    @Test
    public void oldFashionedConcurrency() throws ExecutionException, InterruptedException {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            Future<Integer> squareFuture = executor.submit(new SquareTask(12));
            Future<Integer> sqrtFuture = executor.submit(new SqrtTask(144, 2000));
            Future<String> textFuture = executor.submit(new PrettyTextTask("iTechArt", 1500));

            Integer square = squareFuture.get();
            Integer sqrt = sqrtFuture.get();
            String text = textFuture.get();

            System.out.printf("SQUARE = %d ; SQRT = %d ; TEXT = %s ;%n", square, sqrt, text);
        }
    }

    @Test
    public void structuredConcurrency() throws InterruptedException, ExecutionException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Future<Integer> squareFuture = scope.fork(new SquareTask(12));
            Future<Integer> sqrtFuture = scope.fork(new SqrtTask(144, 2000));
            Future<String> textFuture = scope.fork(new PrettyTextTask("iTechArt", 1500));

            scope.join();
            scope.throwIfFailed();

            Integer square = squareFuture.resultNow();
            Integer sqrt = sqrtFuture.resultNow();
            String text = textFuture.resultNow();

            System.out.printf("SQUARE = %d ; SQRT = %d ; TEXT = %s ;%n", square, sqrt, text);
        }
    }

    @Test(expected = ExecutionException.class)
    public void oldFashionedConcurrencyFailing() throws ExecutionException, InterruptedException {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            Future<Integer> squareFuture = executor.submit(new SquareTask(12, 1000));
            Future<Integer> sqrtFuture = executor.submit(new SqrtTask(144, 2000));
            Future<Void> voidFuture = executor.submit(new FailingTask(null, 500));

            Integer square = 0;
            Integer sqrt = 0;
            Void unused = null;
            try {
                square = squareFuture.get();
                sqrt = sqrtFuture.get();
                unused = voidFuture.get();
            } catch (ExecutionException e) {
                System.out.printf("There was an exception %s%n", e.getMessage());
                throw e;
            }

            System.out.printf("SQUARE = %d ; SQRT = %d ; TEXT = %s ;%n", square, sqrt, unused);
        }
    }

    @Test(expected = ExecutionException.class)
    public void structuredConcurrencyFailing() throws InterruptedException, ExecutionException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Future<Integer> squareFuture = scope.fork(new SquareTask(12, 1000));
            Future<Integer> sqrtFuture = scope.fork(new SqrtTask(144, 2000));
            Future<Void> voidFuture = scope.fork(new FailingTask(null, 500));

            scope.join();
            scope.throwIfFailed();

            Integer square = squareFuture.resultNow();
            Integer sqrt = sqrtFuture.resultNow();
            Void unused = voidFuture.resultNow();

            System.out.printf("SQUARE = %d ; SQRT = %d ; TEXT = %s ;%n", square, sqrt, unused);
        }
    }


}
