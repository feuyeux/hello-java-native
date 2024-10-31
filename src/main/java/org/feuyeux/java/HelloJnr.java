package org.feuyeux.java;

import jnr.ffi.LibraryLoader;

public class HelloJnr {
  public interface CLibrary {
    String sayHello(String name);
  }

  public static void main(String[] args) {
    // cp lib/libhello.dylib /usr/local/lib
    CLibrary clib = LibraryLoader.create(CLibrary.class).load("hello");
    String jnrFfi = clib.sayHello("JNR FFI");
    System.out.println("Java output: " + jnrFfi);
  }
}