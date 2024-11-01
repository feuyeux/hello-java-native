#!/bin/bash
cd "$(
    cd "$(dirname "$0")" >/dev/null 2>&1
    pwd -P
)/" || exit
set -e
cd ..

echo "== JNR =="

if [ -d "jnr_lib" ]; then
    rm -rf jnr_lib
fi
mkdir jnr_lib
cp src/main/c/hello.c jnr_lib

if [ -d "jnr_coon" ]; then
    rm -rf jnr_coon
fi
mkdir jnr_coon

OS_TYPE=$(uname)
if [ "$OS_TYPE" = "Darwin" ]; then
    gcc -fPIC -shared -o jnr_coon/libhello.dylib jnr_lib/hello.c
elif [ "$OS_TYPE" = "Linux" ]; then
    gcc -fPIC -shared -o jnr_coon/libhello.so jnr_lib/hello.c
else
    echo "Unsupported OS: $OS_TYPE"
    exit 1
fi

# build java
export VERSION=2.2.13
export JFFI_VERSION=1.3.10
export ASM_VERSION=9.2
export JNR_PATH=$HOME/.m2/repository/com/github/jnr/jnr-ffi/$VERSION/jnr-ffi-$VERSION.jar
export JFFI_PATH=$HOME/.m2/repository/com/github/jnr/jffi/$JFFI_VERSION/jffi-$JFFI_VERSION.jar
export JFFI_NATIVE_PATH=$HOME/.m2/repository/com/github/jnr/jffi/$JFFI_VERSION/jffi-$JFFI_VERSION-native.jar
export ASM_PATH=$HOME/.m2/repository/org/ow2/asm/asm/$ASM_VERSION/asm-$ASM_VERSION.jar

javac -cp ./jnr_coon:"$JNR_PATH" -d jnr_lib src/main/java/org/feuyeux/java/HelloJnr.java

# run
cd jnr_lib
java -cp ./:../jnr_coon:"$JNR_PATH":"$JFFI_PATH":"$JFFI_NATIVE_PATH":"$ASM_PATH" \
    -Djava.library.path=../jnr_coon \
    org.feuyeux.java.HelloJnr
