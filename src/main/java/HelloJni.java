public class HelloJni {
    // export LD_LIBRARY_PATH=...
    static {
        System.loadLibrary("hello");
    }

    private native String sayHello(String name);

    public static void main(String[] args) {
        String jniResult = new HelloJni().sayHello("JNI");
        System.out.println("Java output:" + jniResult);
    }
}