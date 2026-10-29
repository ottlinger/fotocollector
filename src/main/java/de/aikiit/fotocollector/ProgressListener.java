package de.aikiit.fotocollector;

/** Receives synchronous progress snapshots. Library callers are silent by default. */
@FunctionalInterface
public interface ProgressListener {
    ProgressListener NONE = update -> { };

    void onProgress(ProgressUpdate update);
}
