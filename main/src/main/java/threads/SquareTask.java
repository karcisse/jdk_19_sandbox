package threads;

public class SquareTask extends DelayedTask<Integer> {

    public SquareTask(int id) {
        super(id);
    }

    public SquareTask(Integer id, int delay) {
        super(id, delay);
    }

    @Override
    public Integer call() {
        Integer id = super.call();
        return id * id;
    }
}
