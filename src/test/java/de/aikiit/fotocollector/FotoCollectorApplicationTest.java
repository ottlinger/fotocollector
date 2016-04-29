package de.aikiit.fotocollector;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @author hirsch
 * @version 2016-04-28, 23:13
 */
public class FotoCollectorApplicationTest {
    @Test
    public void launchWithoutArgs() throws IOException {
        FotoCollectorApplication.main(null);

        final File output = new File(".", "fotocollector.html");
        assertTrue(output.exists());
        assertTrue(output.delete());
    }
}
