package threads;

public class SqrtTask extends DelayedTask<Integer> {

    public SqrtTask(int id) {
        super(id);
    }

    public SqrtTask(Integer id, int delay) {
        super(id, delay);
    }

    @Override
    public Integer call() {
        Integer id = super.call();
        return Double.valueOf(Math.sqrt(id)).intValue();
    }
}
