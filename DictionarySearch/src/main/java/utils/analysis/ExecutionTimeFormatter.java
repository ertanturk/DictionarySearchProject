package main.java.utils.analysis;

public class ExecutionTimeFormatter {
  private int decimalPlaces = 3;

  public ExecutionTimeFormatter() {
  }

  public ExecutionTimeFormatter(int decimalPlaces) {
    this.decimalPlaces = decimalPlaces;
  }

  public String formatNanoseconds(long nanoseconds) {
    return String.format("%." + decimalPlaces + "f ns", (double) nanoseconds);
  }

  public String formatMilliseconds(double milliseconds) {
    return String.format("%." + decimalPlaces + "f ms", milliseconds);
  }

  public String formatSeconds(double seconds) {
    return String.format("%." + decimalPlaces + "f s", seconds);
  }

  public String formatSummary(String label, long nanoseconds) {
    double milliseconds = nanoseconds / 1_000_000.0;
    double seconds = nanoseconds / 1_000_000_000.0;

    return String.format(
        "%s: %s (%s, %s)",
        label,
        formatNanoseconds(nanoseconds),
        formatMilliseconds(milliseconds),
        formatSeconds(seconds));
  }

  public String formatComparison(String[] labels, Long[] timesInNanoseconds) {
    if (labels == null || timesInNanoseconds == null) {
      throw new IllegalArgumentException("Labels and times must not be null");
    }
    if (labels.length != timesInNanoseconds.length) {
      throw new IllegalArgumentException("Labels and times must have same length");
    }
    if (labels.length == 0) {
      return "No performance data available.\n";
    }

    StringBuilder sb = new StringBuilder();

    long fastestTime = Long.MAX_VALUE;
    for (long time : timesInNanoseconds) {
      if (time >= 0 && time < fastestTime) {
        fastestTime = time;
      }
    }

    if (fastestTime <= 0) {
      fastestTime = 1;
    }

    for (int i = 0; i < labels.length; i++) {
      String label = labels[i];
      long time = timesInNanoseconds[i];

      if (time == fastestTime) {
        sb.append(String.format(
            "%s: %s (FASTEST)\n",
            label,
            formatNanoseconds(time)));
      } else {
        double ratio = (double) time / fastestTime;
        sb.append(String.format(
            "%s: %s (%.2fx slower)\n",
            label,
            formatNanoseconds(time),
            ratio));
      }
    }

    return sb.toString();
  }

}
