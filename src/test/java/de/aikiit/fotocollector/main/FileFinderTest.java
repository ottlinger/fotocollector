package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author hirsch
 * @version 2016-05-23, 00:06
 */
@ExtendWith(MockitoExtension.class)
public class FileFinderTest {
    @Mock
    private PrintStream console;

    @TempDir
    File folder;

    @Test
    public void emptyResultAndNullParameters() throws IOException {
        System.setOut(console);
        FileFinder.scan(null);
        FileFinder.scan(new ScanResult());
        verify(console, times(2)).println(contains("Nothing to do"));
    }

    @Test
    public void scanForCreatedResult() {
        // TODO
    }
}
