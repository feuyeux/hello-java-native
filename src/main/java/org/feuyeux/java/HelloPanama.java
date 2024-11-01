package org.feuyeux.java;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloPanama {
    public static void main(String[] args) {
        // mkdir build
        // gcc -shared -o build/libhello.dylib -fPIC src/main/c/hello.c
        Path libraryPath = Paths.get("/Users/hanl5/coding/feuyeux/hello-java-native/build/libhello.dylib");
        System.out.println("Loading library from: " + libraryPath.toAbsolutePath());
        System.load(libraryPath.toAbsolutePath().toString());

        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.loaderLookup();

        var symbolOptional = lookup.find("sayHello");
        if (symbolOptional.isEmpty()) {
            System.err.println("Could not find 'sayHello' symbol in the library");
            return;
        }
        MemorySegment sayHello = symbolOptional.get();

        // 创建函数描述符
        FunctionDescriptor fd = FunctionDescriptor.of(
                ValueLayout.ADDRESS,
                ValueLayout.ADDRESS
        );

        MethodHandle sayHelloHandle = linker.downcallHandle(sayHello, fd);

        try {
            String name = "Panama";
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment nameStr = arena.allocateUtf8String(name);
                MemorySegment resultPtr = (MemorySegment) sayHelloHandle.invoke(nameStr);

                // 将返回的指针转换为受限段
                MemorySegment resultSegment = resultPtr.reinterpret(1024, arena, null);
                String greeting = resultSegment.getUtf8String(0);
                System.out.println("Java output: " + greeting);

                // 获取free函数来释放C分配的内存
                MemorySegment free = lookup.find("free").orElseThrow();
                MethodHandle freeHandle = linker.downcallHandle(
                        free,
                        FunctionDescriptor.ofVoid(ValueLayout.ADDRESS)
                );
                freeHandle.invoke(resultPtr);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}