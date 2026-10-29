package de.aikiit.fotocollector;

import java.io.PrintStream;
import java.time.Duration;
import java.util.function.LongSupplier;

/**
 * CLI-only rendering on stderr, leaving stdout available for normal output.
 * Interactive terminals reuse one line (at most five updates/second); redirected
 * output gets plain status lines at most every five seconds. Start and final states
 * are always printed. The injected clock keeps throttling deterministic in tests.
 */
public final class ConsoleProgress implements ProgressListener {
    private final PrintStream output;
    private final boolean interactive;
    private final LongSupplier clock;
    private final long interval;
    private boolean started;
    private long lastUpdate;
    private int lineWidth;

    public ConsoleProgress() {
        this(System.err, System.console() != null, System::nanoTime);
    }

    ConsoleProgress(PrintStream output, boolean interactive, LongSupplier clock) {
        this.output = output;
        this.interactive = interactive;
        this.clock = clock;
        this.interval = Duration.ofMillis(interactive ? 200 : 5000).toNanos();
    }

    @Override
    public void onProgress(ProgressUpdate update) {
        long now = clock.getAsLong();
        boolean finished = update.state() != ProgressUpdate.State.RUNNING;
        if (started && !finished && now - lastUpdate < interval) {
            return;
        }
        String message = format(update);
        if (interactive) {
            output.print("\r" + message + " ".repeat(Math.max(0, lineWidth - message.length())));
            lineWidth = message.length();
            if (finished) {
                output.println();
                lineWidth = 0;
            }
        } else {
            output.println(message);
        }
        output.flush();
        lastUpdate = now;
        started = !finished;
    }

    private static String format(ProgressUpdate update) {
        String label = update.operation() == ProgressUpdate.Operation.SCAN ? "Scan" : "Collect";
        if (update.state() == ProgressUpdate.State.COMPLETED) {
            label += " complete";
        } else if (update.state() == ProgressUpdate.State.FAILED) {
            label += " failed";
        }
        if (update.total() < 0) {
            return label + ": counting files...";
        }
        if (update.operation() == ProgressUpdate.Operation.SCAN) {
            return "%s: %d of %d files checked | %d images found".formatted(
                    label, update.current(), update.total(), update.found());
        }
        return "%s: %d of %d images copied | %d missing".formatted(
                label, update.current(), update.total(), update.missing());
    }
}
