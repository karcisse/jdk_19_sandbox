import jdk.incubator.concurrent.StructuredTaskScope;
import threads.PrettyTextTask;
import threads.SqrtTask;
import threads.SquareTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StructureConcurrencySandbox implements Sandbox {
    @Override
    public void play() {
        try {
//            oldFashionedConcurrency();
            structuredConcurrency();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void oldFashionedConcurrency() throws ExecutionException, InterruptedException {
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

    private void structuredConcurrency() throws ExecutionException, InterruptedException {
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
}
