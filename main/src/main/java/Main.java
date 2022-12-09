public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Sandbox sandbox;

//        sandbox = new VirtualThreadsSandbox();
//        sandbox = new ForeignFunctionAndMemoryAPISandbox();
//        sandbox = new StructureConcurrencySandbox();
//        sandbox = new PatternMatchingSandbox();
        sandbox = new RecordPatternSandbox();

        sandbox.play();
    }
}