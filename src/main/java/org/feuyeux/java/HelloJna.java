package org.feuyeux.java;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class HelloJna {
    public interface CLibrary extends Library {
        // cp lib/libhello.dylib src/main/resources/
        CLibrary clib = Native.load("hello", CLibrary.class);

        String sayHello(String name);
    }

    public static String execute(String name) {
        return CLibrary.clib.sayHello(name);
    }

    public static void main(String[] args) {
        String jnaResult = HelloJna.execute("JNA");
        System.out.println("Java output: " + jnaResult);
    }
}