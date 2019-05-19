package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.rules.TemporaryFolder;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author hirsch
 * @version 2016-05-23, 00:06
 */
@RunWith(MockitoJUnitRunner.class)
public class FileFinderTest {
    @Mock
    private PrintStream console;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void emptyResultAndNullParameters() throws IOException {
        System.setOut(console);
        FileFinder.scan(null);
        FileFinder.scan(new ScanResult());
        verify(console, times(2)).println(contains("Nothing to do"));
    }

    @Test
    public void scanForCreatedResult() {
        // tbd
    }
}
