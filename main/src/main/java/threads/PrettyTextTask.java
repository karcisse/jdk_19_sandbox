package threads;

public class PrettyTextTask extends DelayedTask<String> {

    public PrettyTextTask(String text) {
        super(text);
    }

    public PrettyTextTask(String id, int delay) {
        super(id, delay);
    }

    @Override
    public String call() {
        String text = super.call();
        return "<<< " + text + " >>>";
    }
}
