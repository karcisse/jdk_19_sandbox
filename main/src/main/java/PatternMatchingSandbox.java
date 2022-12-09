public class PatternMatchingSandbox implements Sandbox {
    @Override
    public void play() {
        newSwitch("Very long switch");
        newSwitch("SHRT");
        newSwitch(12);
    }

    private void newSwitch(Object obj) {
        switch (obj) {
            case String s when s.length() > 5 -> System.out.println(s.toUpperCase());
            case String s                     -> System.out.println(s.toLowerCase());

            case Integer i                    -> System.out.println(i * i);

            default -> {}
        }
    }

//    private void oldSwitch(Object obj) {
//        switch (obj) {
//            case String s && s.length() > 5 -> System.out.println(s.toUpperCase());
//            case String s                   -> System.out.println(s.toLowerCase());
//
//            case Integer i                  -> System.out.println(i * i);
//
//            default -> {}
//        }
//    }
}
