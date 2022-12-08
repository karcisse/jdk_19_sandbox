import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.*;

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

       printf("foreign call len = %d\n", len);
    }

    private void printf(String format, Long value) {
        SymbolLookup stdlib = Linker.nativeLinker().defaultLookup();
        MethodHandle printf = Linker.nativeLinker().downcallHandle(
                stdlib.lookup("printf").orElseThrow(),
                FunctionDescriptor.of(JAVA_INT, ADDRESS).asVariadic(JAVA_LONG)
        );

        SegmentAllocator malloc = SegmentAllocator.implicitAllocator();

        try {
            MemorySegment s = malloc.allocateUtf8String(format);
            printf.invoke(s, value);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
