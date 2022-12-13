import org.junit.Test;

public class PatternMatchingTest {

    @Test
    public void test() {
        newSwitch("Very long switch");
        newSwitch("SHRT");
        newSwitch(12);
    }

    private void newSwitch(Object obj) {
        switch (obj) {
            case String s when s.length() > 5 -> System.out.println(s.toUpperCase());
            case String s                     -> System.out.println(s.toLowerCase());

            case Integer i                    -> System.out.println(i * i);

            default -> System.out.println("Unknown object");
        }
    }
}
