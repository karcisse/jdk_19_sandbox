package threads;

public class SquareTask extends DelayedTask<Integer> {

    public SquareTask(int id) {
        super(id);
    }

    @Override
    public Integer call() {
        Integer id = super.call();
        return id * id;
    }
}
