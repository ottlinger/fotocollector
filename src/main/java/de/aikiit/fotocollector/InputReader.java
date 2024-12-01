package de.aikiit.fotocollector;

import java.nio.file.Path;

/**
 * @author hirsch
 * @version 2016-05-28, 20:45
 */
public interface InputReader {
    /**
     * Reads a given collector file and transforms it into a scan result.
     *
     * @param input fotoCollector input file.
     * @return scan result, is never {@code null} and can be empty in case of errors.
     */
    ScanResult read(Path input);
}
