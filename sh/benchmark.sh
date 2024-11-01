#!/bin/bash
cd "$(
    cd "$(dirname "$0")" >/dev/null 2>&1
    pwd -P
)/" || exit
set -e
cd ..

echo "== Benchmark =="

OS_TYPE=$(uname)
if [ "$OS_TYPE" = "Darwin" ]; then
    export jni_coon_path=/Users/hanl5/coding/feuyeux/hello-java-native/jni_coon
    export JAVA_LIBRARY_PATH=$JAVA_LIBRARY_PATH:$jni_coon_path
elif [ "$OS_TYPE" = "Linux" ]; then
    export jni_coon_path=/mnt/d/coding/hello-java-native/jni_coon
    export LD_LIBRARY_PATH=$jni_coon_path:$LD_LIBRARY_PATH
else
    echo "Unsupported OS: $OS_TYPE"
    exit 1
fi

mvn test -Dtest=org.feuyeux.java.benchmark.HelloBenchmark
