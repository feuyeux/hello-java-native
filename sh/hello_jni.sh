#!/bin/bash
cd "$(
  cd "$(dirname "$0")" >/dev/null 2>&1
  pwd -P
)/" || exit
set -e
cd ..

echo "== JNI =="

# generate C header file
if [ -d "jni_lib" ]; then
  rm -rf jni_lib
fi
mkdir jni_lib
javac -h jni_lib src/main/java/org/feuyeux/java/HelloJni.java

# compile C source file
if [ -d "jni_coon" ]; then
  rm -rf jni_coon
fi
mkdir jni_coon

cp src/main/c/hello_jni.c jni_lib

OS_TYPE=$(uname)
if [ "$OS_TYPE" = "Darwin" ]; then
  gcc -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" -dynamiclib -o jni_coon/libhello.dylib jni_lib/hello_jni.c
elif [ "$OS_TYPE" = "Linux" ]; then
  gcc -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" -shared -o jni_coon/libhello.so jni_lib/hello_jni.c
else
  echo "Unsupported OS: $OS_TYPE"
  exit 1
fi

# build java
javac -cp ./jni_coon -d jni_lib src/main/java/org/feuyeux/java/HelloJni.java

# run
cd jni_lib
java -Djava.library.path=../jni_coon org.feuyeux.java.HelloJni
