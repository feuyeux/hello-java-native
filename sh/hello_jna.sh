#!/bin/bash
cd "$(
    cd "$(dirname "$0")" >/dev/null 2>&1
    pwd -P
)/" || exit
set -e
cd ..

echo "== JNA =="

if [ -d "jna_lib" ]; then
    rm -rf jna_lib
fi
mkdir jna_lib
cp src/main/c/hello.c jna_lib

if [ -d "jna_coon" ]; then
    rm -rf jna_coon
fi
mkdir jna_coon

OS_TYPE=$(uname)
if [ "$OS_TYPE" = "Darwin" ]; then
    gcc -fPIC -shared -o jna_coon/libhello.dylib jna_lib/hello.c
elif [ "$OS_TYPE" = "Linux" ]; then
    gcc -fPIC -shared -o jna_coon/libhello.so jna_lib/hello.c
else
    echo "Unsupported OS: $OS_TYPE"
    exit 1
fi

# build java
export VERSION=5.15.0
export JNA_PATH=$HOME/.m2/repository/net/java/dev/jna/jna/$VERSION/jna-$VERSION.jar
javac -cp "$JNA_PATH" -d jna_lib src/main/java/org/feuyeux/java/HelloJna.java

# run
cd jna_lib
java -cp ./:$JNA_PATH:../jna_coon org.feuyeux.java.HelloJna
