package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author hirsch
 * @version 2016-05-23, 00:06
 */
@ExtendWith(MockitoExtension.class)
public class FileFinderTest {
    @TempDir
    File folder;

    @Test
    public void emptyResultAndNullParameters() throws IOException {
        FileFinder.scan(null);
        FileFinder.scan(new ScanResult());
    }

    @Test
    public void scanForCreatedResult() {
        // TODO
    }
}
