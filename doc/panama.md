
jextract

```sh
git clone https://github.com/openjdk/jextract.git
cd jextract/
gradle -Pjdk21_home=/Library/Java/JavaVirtualMachines/openjdk-21.jdk/Contents/Home -Pllvm_home=/usr/local/opt/llvm clean verify
build/jextract/bin/jextract --version
```