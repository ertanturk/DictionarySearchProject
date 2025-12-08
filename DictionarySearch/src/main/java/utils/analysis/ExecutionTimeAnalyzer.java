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
    start();
    task.run();
    stop();
    return getElapsedTimeInNanoseconds();
  }
}
