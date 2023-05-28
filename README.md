# Demo project to reproduce Log4j2 issue #1490

**Possible concurrency bug in DatePatternConverter.formatWithoutThreadLocals(Instant,StringBuilder)**
https://github.com/apache/logging-log4j2/issues/1490

This Java application will create a shared `DatePatternConveter` using the pattern "HH:mm:ss.SSS" and then spawn 10 threads.
Each thread will do 1000 iterations where it tries to format a thread-specific `MutableInstant` and compare the result with the expected value.
If the result is not as expected, the thread will print a message to the console.

### How to build and run

```shell
mvn clean package
java -jar target/log4j2-cached-time-demo-1.0-SNAPSHOT.jar
```

#### Expected output

As long as the bug is present in `DatePatternConveter`, the output will be something like this:

```plain
Thread 4 expected 00:00:00.004 but got 00:00:00.001 in iteration 0
Thread 7 expected 00:00:00.007 but got 00:00:00.001 in iteration 0
Thread 5 expected 00:00:00.005 but got 00:00:00.001 in iteration 0
Thread 3 expected 00:00:00.003 but got 00:00:00.001 in iteration 0
Thread 3 expected 00:00:00.003 but got 00:00:00.007 in iteration 1
Thread 3 expected 00:00:00.003 but got 00:00:00.004 in iteration 2
Thread 3 expected 00:00:00.003 but got 00:00:00.007 in iteration 3
Thread 3 expected 00:00:00.003 but got 00:00:00.004 in iteration 4
Thread 5 expected 00:00:00.005 but got 00:00:00.004 in iteration 1
Thread 9 expected 00:00:00.009 but got 00:00:00.001 in iteration 0
[...]
Thread 8 expected 00:00:00.008 but got 00:00:00.002 in iteration 196
Thread 3 expected 00:00:00.003 but got 00:00:00.002 in iteration 9
Thread 8 expected 00:00:00.008 but got 00:00:00.006 in iteration 197
Thread 0 expected 00:00:00.000 but got 00:00:00.002 in iteration 10
Thread 7 expected 00:00:00.007 but got 00:00:00.002 in iteration 938
Thread 3 expected 00:00:00.003 but got 00:00:00.008 in iteration 36
Thread 7 expected 00:00:00.007 but got 00:00:00.000 in iteration 939
Thread 3 expected 00:00:00.003 but got 00:00:00.008 in iteration 37
Thread 7 expected 00:00:00.007 but got 00:00:00.008 in iteration 940
Thread 3 expected 00:00:00.003 but got 00:00:00.000 in iteration 38
```

