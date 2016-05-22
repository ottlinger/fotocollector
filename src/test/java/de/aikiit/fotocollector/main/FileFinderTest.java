package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanResult;
import org.junit.Test;

import java.io.IOException;

/**
 * @author hirsch
 * @version 2016-05-23, 00:06
 */
public class FileFinderTest {

    @Test
    public void emptyResultAndNullParameters() throws IOException {
        FileFinder.scan(null);
        FileFinder.scan(new ScanResult());
    }
}
