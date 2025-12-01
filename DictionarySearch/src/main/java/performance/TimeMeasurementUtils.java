package main.java.performance;

public class TimeMeasurementUtils {

  public static final double nsToSeconds(long nanoseconds) {
    return nanoseconds / 1_000_000_000.0;
  }

  public static final void measure(String methodName, int dataSize, Runnable callback) {

    final int warmupRuns = 5;
    final int measuredRuns = 10;

    for (int i = 0; i < warmupRuns; i++) {
      callback.run();
    }

    long totalTime = 0;
    for (int i = 0; i < measuredRuns; i++) {
      long start = System.nanoTime();
      callback.run();
      totalTime += (System.nanoTime() - start);
    }

    double avgTimeSeconds = nsToSeconds(totalTime) / measuredRuns;
    printMeasurement(methodName, dataSize, avgTimeSeconds, measuredRuns);
  }

  public static final void printMeasurement(String methodName, int dataSize, double timeSeconds, int runs) {
    String title = " Performance Measurement ";
    String line = "Method: " + methodName + " | Data Size: " + dataSize + " | Runs: " + runs + " | Avg Time: "
        + String.format("%.6f", timeSeconds) + " seconds ";
    int lineLength = Math.max(title.length(), line.length());
    int centeredTitlePadding = (lineLength - title.length()) / 2;
    title = " ".repeat(centeredTitlePadding) + title + " ".repeat(lineLength - title.length() - centeredTitlePadding);
    System.out.println("*".repeat(lineLength));
    System.out.println(" ".repeat(lineLength));
    System.out.println(title);
    System.out.println(" ".repeat(lineLength));
    System.out.println("*".repeat(lineLength));
    System.out.println(" ".repeat(lineLength));
    System.out.println(line);
    System.out.println(" ".repeat(lineLength));
    System.out.println("*".repeat(lineLength));
    System.out.println();
  }

}
