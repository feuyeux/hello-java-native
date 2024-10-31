package org.feuyeux.java;

public class HelloJni {
    // export LD_LIBRARY_PATH=...
    static {
        System.loadLibrary("hello");
    }

    public native String sayHello(String name);

    public static void main(String[] args) {
        HelloJni helloJni = new HelloJni();
        String jniResult = helloJni.sayHello("JNI");
        System.out.println("Java output:" + jniResult);
    }
}