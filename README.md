# Java Native

## 0 Reference

**JNI**(Java Native Interface) <https://docs.oracle.com/en/java/javase/19/docs/specs/jni/>

```
                User Code
                    |
Java            JNI call
--------------------|---------------------
C/native        JNI impl
                    |
                Target Library
```

**JNA**(Java Native Library) <https://github.com/java-native-access/jna>

**JNR**(Java Native Runtime) FFI(Foreign Function Interface) <https://github.com/jnr/jnr-ffi>

```
                User Code
                    |
                JNA/JNR stub
                    |
Java            JNI call
--------------------|---------------------
C/native        JNI impl
                    |
                  libffi
                    |
                Target Library
```

## 1 JNI

### 1.1 Java code

`HelloJni.java`

```java
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
```

### 1.2 generate C head `HelloJni.h`

```sh
# generate head
javac -h jni_lib src/main/java/HelloJni.java
```

### 1.3 C code `hello_jni.c`

```c
#include <jni.h>      // JNI header provided by JDK
#include <stdio.h>    // C Standard IO Header
#include "HelloJni.h" // Generated

// Implementation of the native method sayHello()
JNIEXPORT jstring JNICALL Java_HelloJni_sayHello(JNIEnv *env, jobject thisObj, jstring j_str)
{
    const jchar *c_str = NULL;
    char cs[128] = "Hello ";
    char *pBuff = cs + 6;
    c_str = (*env)->GetStringCritical(env, j_str, NULL);
    if (c_str != NULL)
    {
        while (*c_str)
        {
            *pBuff++ = *c_str++;
        }
        (*env)->ReleaseStringCritical(env, j_str, c_str);
        printf("C output:%s\n", cs);
    }
    return (*env)->NewStringUTF(env, cs);
}
```

### 1.4 build

`libhello.dylib`

```sh
# build c
mkdir jni_coon
gcc -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" -dynamiclib -o jni_coon/libhello.dylib jni_lib/hello_jni.c
```

```sh
# build java
javac -cp ./jni_coon -d jni_coon src/main/java/HelloJni.java
```

### 1.5 run

```sh
cd jni_coon && java -Djava.library.path=. HelloJni
```

```sh
C output:Hello JNI
Java output:Hello JNI
```

## 2 JNA/JNR

### 2.1 C code

```sh
mkdir jna_lib
cd jna_lib
code hello.c
```

`hello.c`

```c
#include <stdio.h>

char* sayHello(char *name)
{
    static char buffer[50];
    snprintf(buffer, sizeof(buffer), "Hello %s", name);
    printf("C output: %s\n", buffer);
    return buffer;
}
```

### 2.2 build `libhello.dylib`

```sh
cd jna_lib
gcc -fPIC -shared -o libhello.dylib hello.c
```

### 2.3-1 JNA

```java
import com.sun.jna.Library;
import com.sun.jna.Native;

public class HelloJna {
    public interface CLibrary extends Library {
        // cp lib/libhello.dylib src/main/resources/
        CLibrary clib = Native.load("hello", CLibrary.class);

        String sayHello(String name);
    }

    public static void main(String[] args) {
        String jnaResult = CLibrary.clib.sayHello("JNA");
        System.out.println("Java output:" + jnaResult);
    }
}
```

```sh
# ls $HOME/.m2/repository/net/java/dev/jna/jna
export VERSION=5.13.0
export JNA_PATH=$HOME/.m2/repository/net/java/dev/jna/jna/$VERSION/jna-$VERSION.jar
mkdir jna_coon
javac -cp $JNA_PATH -d jna_coon src/main/java/HelloJna.java 
java -cp ./jna_lib:$JNA_PATH:./jna_coon HelloJna
```

```sh
C output: Hello JNA
Java output: Hello JNA
```

### 2.3-2 JNR

```sh
mvn dependency:tree |grep jnr -A 10

[INFO] \- com.github.jnr:jnr-ffi:jar:2.2.13:compile
[INFO]    +- com.github.jnr:jffi:jar:1.3.10:compile
[INFO]    +- com.github.jnr:jffi:jar:native:1.3.10:runtime
[INFO]    +- org.ow2.asm:asm:jar:9.2:compile
[INFO]    +- org.ow2.asm:asm-commons:jar:9.2:compile
[INFO]    +- org.ow2.asm:asm-analysis:jar:9.2:compile
[INFO]    +- org.ow2.asm:asm-tree:jar:9.2:compile
[INFO]    +- org.ow2.asm:asm-util:jar:9.2:compile
[INFO]    +- com.github.jnr:jnr-a64asm:jar:1.0.0:compile
[INFO]    \- com.github.jnr:jnr-x86asm:jar:1.0.2:compile
```

```sh
export VERSION=2.2.13
export JFFI_VERSION=1.3.10
export ASM_VERSION=9.2
export JNR_PATH=$HOME/.m2/repository/com/github/jnr/jnr-ffi/$VERSION/jnr-ffi-$VERSION.jar
export JFFI_PATH=$HOME/.m2/repository/com/github/jnr/jffi/$JFFI_VERSION/jffi-$JFFI_VERSION.jar
export JFFI_NATIVE_PATH=$HOME/.m2/repository/com/github/jnr/jffi/$JFFI_VERSION/jffi-$JFFI_VERSION-native.jar
export ASM_PATH=$HOME/.m2/repository/org/ow2/asm/asm/$ASM_VERSION/asm-$ASM_VERSION.jar
```

```sh
mkdir jnr_coon
javac -cp $JNR_PATH -d jnr_coon src/main/java/HelloJnr.java
java -cp ./jna_lib:$JNR_PATH:$JFFI_PATH:$JFFI_NATIVE_PATH:$ASM_PATH:./jnr_coon -Djava.library.path=jna_lib HelloJnr
```

```sh
C output: Hello JNR FFI
Java output: Hello JNR FFI
```
