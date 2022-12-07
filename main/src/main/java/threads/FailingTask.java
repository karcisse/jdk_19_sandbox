package threads;

public class FailingTask extends DelayedTask<Void> {

    public FailingTask(Void id, int delay) {
        super(id, delay);
    }

    @Override
    public Void call() {
        super.call();
        throw new RuntimeException("I'm failing as expected");
    }
}
