package org.feuyeux.java;

import jnr.ffi.LibraryLoader;

public class HelloJnr {
    public interface CLibrary {
        String sayHello(String name);
    }

    public static String execute(String name) {
        CLibrary clib = LibraryLoader.create(CLibrary.class).load("hello");
        return clib.sayHello(name);
    }

    public static void main(String[] args) {
        // cp lib/libhello.dylib /usr/local/lib
        String jnrFfi = execute("JNR FFI");
        System.out.println("Java output: " + jnrFfi);
    }
}