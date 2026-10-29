package de.aikiit.fotocollector;

/**
 * A progress snapshot independent of the UI. For SCAN, current counts checked files
 * and found counts detected images; for COLLECT, both count successfully copied entries.
 * A total of -1 means the scanner is still counting files. Totals are a snapshot of
 * the directory before detection, not a filesystem lock: files may change during a scan.
 * FAILED is distinct from COMPLETED; missing collect entries do not count as copies.
 */
public record ProgressUpdate(Operation operation, long current, long total, long found,
                             long missing, State state) {
    public enum Operation { SCAN, COLLECT }
    public enum State { RUNNING, COMPLETED, FAILED }
}
