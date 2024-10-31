package org.feuyeux.java.benchmark;

import org.feuyeux.java.HelloJna;
import org.feuyeux.java.HelloJni;
import org.feuyeux.java.HelloJnr;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Fork(1)
@Warmup(iterations = 3, time = 3500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
public class HelloBenchmark {
    @Benchmark
    public String benchmarkHelloJna() {
        System.setProperty("jna.library.path", "./jna_coon");
        return HelloJna.execute("JNA");
    }

    @Benchmark
    public String benchmarkHelloJni() {
        HelloJni helloJni = new HelloJni();
        return helloJni.sayHello("JNI");
    }

    @Benchmark
    public String benchmarkHelloJnr() {
        System.setProperty("jnr.ffi.library.path", "./jna_coon");
        return HelloJnr.execute("JNR");
    }

    @Test
    public void test() throws RunnerException {
        Options opt = new OptionsBuilder().include(HelloBenchmark.class.getSimpleName()).result("benchmark.json").resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }
}