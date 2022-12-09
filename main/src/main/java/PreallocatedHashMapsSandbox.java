import java.util.HashMap;
import java.util.Map;

public class PreallocatedHashMapsSandbox implements Sandbox {


    @Override
    public void play() {
        // for 120 mappings: 120 / 0.75 = 160
        Map<String, Integer> mapAllocatedBefore = new HashMap<>(160);

        Map<String, Integer> mapAllocatedNow = HashMap.newHashMap(120);
    }
}
