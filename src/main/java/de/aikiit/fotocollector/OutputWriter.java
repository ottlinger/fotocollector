package de.aikiit.fotocollector;

/**
 * @author hirsch
 * @version 2016-02-20, 14:07
 */
public interface OutputWriter {

    /**
     * Transforms a given scan result into a specific output result.
     * Each implementation may define a format that is written from the given scanned files.
     * Example: JSON transformer, HTML transformer.
     * @param result scan result that is to be transformed.
     * @return output result transformed by reading the given scan result.
     */
    OutputResult write(ScanResult result);
}
