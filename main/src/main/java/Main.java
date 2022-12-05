public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Sandbox sandbox;

//        sandbox = new VirtualThreadsSandbox();
        sandbox = new ForeignFunctionAndMemoryAPISandbox();

        sandbox.play();
    }
}