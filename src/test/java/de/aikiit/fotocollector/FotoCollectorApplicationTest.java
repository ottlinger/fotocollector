package de.aikiit.fotocollector;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;

/**
 * @author hirsch
 * @version 2016-04-28, 23:13
 */
public class FotoCollectorApplicationTest {
    @Test
    public void launchWithoutArgsThenNoOutputFilesAreWritten() throws IOException {
        FotoCollectorApplication.main(null);

        for(String fileName : Arrays.asList("fotocollector.html", "fotocollector.json")) {
            final File output = new File(".", fileName);
            assertFalse(output.exists());
        }
    }
}
