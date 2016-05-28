package de.aikiit.fotocollector;

import java.nio.file.Path;

/**
 * @author hirsch
 * @version 2016-05-28, 20:45
 */
public interface InputReader {
    /**
     * Reads a given collector file and transforms it into a scanresult.
     *
     * @param input fotocollector input file.
     * @return scan result, is never null and can be empty in case of errors.
     */
    ScanResult read(Path input);
}
