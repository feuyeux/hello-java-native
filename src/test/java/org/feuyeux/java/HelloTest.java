package org.feuyeux.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloTest {

    @Test
    public void testHelloJni() {
        HelloJni helloJni = new HelloJni();
        String jniResult = helloJni.sayHello("JNI");
        System.out.println("Java output: " + jniResult);
        Assertions.assertEquals("Hello JNI", jniResult);
    }

    @Test
    public void testHelloJna() {
        System.setProperty("jna.library.path", "./jna_coon");
        String jnaResult = HelloJna.execute("JNA");
        System.out.println("Java output: " + jnaResult);
        Assertions.assertEquals("Hello JNA", jnaResult);
    }

    @Test
    public void testHelloJnr() {
        System.setProperty("jnr.ffi.library.path", "./jna_coon");
        String jnrResult = HelloJnr.execute("JNR FFI");
        System.out.println("Java output: " + jnrResult);
        Assertions.assertEquals("Hello JNR FFI", jnrResult);
    }
}