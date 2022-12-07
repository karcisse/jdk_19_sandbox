import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

public class ForeignFunctionAndMemoryAPISandbox implements Sandbox {
    @Override
    public void play() {
        stringLength();
    }

    private void stringLength() {
        SymbolLookup stdlib = Linker.nativeLinker().defaultLookup();

        MethodHandle strlen = Linker.nativeLinker()
                .downcallHandle(stdlib.lookup("strlen")
                                .orElseThrow(),
                        FunctionDescriptor.of(JAVA_LONG, ADDRESS));

        MemorySegment str = SegmentAllocator.implicitAllocator().allocateUtf8String("Hello ITechArt!");

        long len;
        try {
            len = (long) strlen.invoke(str);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        System.out.printf("len = %d%n", len);
    }
}
