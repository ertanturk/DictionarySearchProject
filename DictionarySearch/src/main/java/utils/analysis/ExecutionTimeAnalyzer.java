package main.java.utils.analysis;

public class ExecutionTimeAnalyzer {
  private long startTime;
  private long endTime;

  public void start() {
    this.startTime = System.nanoTime();
  }

  public void stop() {
    this.endTime = System.nanoTime();
  }

  public long getElapsedTimeInNanoseconds() {
    return endTime - startTime;
  }

  public double getElapsedTimeInMilliseconds() {
    return (endTime - startTime) / 1_000_000.0;
  }

  public long run(Runnable task) {
    // JVM warm-up
    for (int i = 0; i < 5; i++) {
      task.run();
    }
    start();
    task.run();
    stop();
    return getElapsedTimeInNanoseconds();
  }

  public long runRepeated(Runnable task, int warmup, int iterations) {
    // JVM warm-up
    for (int i = 0; i < warmup; i++) {
      task.run();
    }

    long start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      task.run();
    }
    return (System.nanoTime() - start) / iterations;
  }
}
