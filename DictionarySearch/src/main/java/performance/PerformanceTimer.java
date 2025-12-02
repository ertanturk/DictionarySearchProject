package main.java.performance;

public final class PerformanceTimer {

  private PerformanceTimer() {
  }

  public static double nanosToSeconds(long nanoseconds) {
    return nanoseconds / 1_000_000_000.0;
  }

  public static double measure(String methodName, int dataSize, Runnable callback) {
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

    double avgTimeSeconds = nanosToSeconds(totalTime) / measuredRuns;
    System.out.println(formatMeasurement(methodName, dataSize, avgTimeSeconds, measuredRuns));
    return avgTimeSeconds;
  }

  public static String formatMeasurement(String methodName, int dataSize, double timeSeconds, int runs) {
    String title = " Performance Measurement ";
    String line = "Method: " + methodName + " | Data Size: " + dataSize + " | Runs: " + runs + " | Avg Time: "
        + String.format("%.6f", timeSeconds) + " seconds ";
    int lineLength = Math.max(title.length(), line.length());
    int centeredTitlePadding = (lineLength - title.length()) / 2;
    title = " ".repeat(centeredTitlePadding) + title + " ".repeat(lineLength - title.length() - centeredTitlePadding);

    StringBuilder builder = new StringBuilder();
    builder.append("*".repeat(lineLength)).append('\n');
    builder.append(" ".repeat(lineLength)).append('\n');
    builder.append(title).append('\n');
    builder.append(" ".repeat(lineLength)).append('\n');
    builder.append("*".repeat(lineLength)).append('\n');
    builder.append(" ".repeat(lineLength)).append('\n');
    builder.append(line).append('\n');
    builder.append(" ".repeat(lineLength)).append('\n');
    builder.append("*".repeat(lineLength)).append('\n');
    builder.append('\n');

    return builder.toString();
  }
}
