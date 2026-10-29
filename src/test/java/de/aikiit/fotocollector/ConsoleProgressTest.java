package de.aikiit.fotocollector;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import static de.aikiit.fotocollector.ProgressUpdate.Operation.*;
import static de.aikiit.fotocollector.ProgressUpdate.State.*;
import static org.assertj.core.api.Assertions.assertThat;

class ConsoleProgressTest {

    private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    private final AtomicLong clock = new AtomicLong();

    private ConsoleProgress renderer(boolean interactive) {
        return new ConsoleProgress(new PrintStream(bytes, true, StandardCharsets.UTF_8), interactive, clock::get);
    }

    private String output() {
        return bytes.toString(StandardCharsets.UTF_8);
    }

    @Test
    void redirectedOutputIsThrottledAndAlwaysIncludesFinalCounts() {
        ConsoleProgress progress = renderer(false);
        progress.onProgress(new ProgressUpdate(COLLECT, 0, 100, 0, 0, RUNNING));
        progress.onProgress(new ProgressUpdate(COLLECT, 1, 100, 1, 0, RUNNING));
        clock.set(Duration.ofSeconds(5).toNanos());
        progress.onProgress(new ProgressUpdate(COLLECT, 42, 100, 42, 0, RUNNING));
        progress.onProgress(new ProgressUpdate(COLLECT, 98, 100, 98, 2, COMPLETED));

        assertThat(output().lines()).containsExactly(
                "Collect: 0 of 100 images copied | 0 missing",
                "Collect: 42 of 100 images copied | 0 missing",
                "Collect complete: 98 of 100 images copied | 2 missing");
        assertThat(output()).doesNotContain("\r", "\u001b", "100 of 100");
    }

    @Test
    void terminalReusesOneLineAndEndsWithANewline() {
        ConsoleProgress progress = renderer(true);
        progress.onProgress(new ProgressUpdate(SCAN, 0, -1, 0, 0, RUNNING));
        progress.onProgress(new ProgressUpdate(SCAN, 0, 20, 0, 0, RUNNING));
        clock.set(Duration.ofMillis(200).toNanos());
        progress.onProgress(new ProgressUpdate(SCAN, 10, 20, 3, 0, RUNNING));
        progress.onProgress(new ProgressUpdate(SCAN, 20, 20, 7, 0, COMPLETED));

        assertThat(output()).startsWith("\rScan: counting files...")
                .contains("\rScan: 10 of 20 files checked | 3 images found")
                .endsWith("\rScan complete: 20 of 20 files checked | 7 images found" + System.lineSeparator());
        assertThat(output().chars().filter(c -> c == '\n').count()).isEqualTo(1);
    }

    @Test
    void failuresAndEmptyOperationsArePrintedEvenWithinThrottleInterval() {
        ConsoleProgress progress = renderer(false);
        progress.onProgress(new ProgressUpdate(COLLECT, 0, 2, 0, 0, RUNNING));
        progress.onProgress(new ProgressUpdate(COLLECT, 1, 2, 1, 0, FAILED));
        progress.onProgress(new ProgressUpdate(SCAN, 0, 0, 0, 0, RUNNING));
        progress.onProgress(new ProgressUpdate(SCAN, 0, 0, 0, 0, COMPLETED));

        assertThat(output().lines()).containsExactly(
                "Collect: 0 of 2 images copied | 0 missing",
                "Collect failed: 1 of 2 images copied | 0 missing",
                "Scan: 0 of 0 files checked | 0 images found",
                "Scan complete: 0 of 0 files checked | 0 images found");
    }
}
