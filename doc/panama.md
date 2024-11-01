
jextract

```sh
git clone https://github.com/openjdk/jextract.git
cd jextract/
gradle -Pjdk21_home=/Library/Java/JavaVirtualMachines/openjdk-21.jdk/Contents/Home -Pllvm_home=/usr/local/opt/llvm clean verify
build/jextract/bin/jextract --version
```

```sh
javac --enable-preview --source 21 src/main/java/org/feuyeux/java/HelloPanama.java                                        1 ↵

java --enable-preview --enable-native-access=ALL-UNNAMED -cp src/main/java org.feuyeux.java.HelloPanama
```