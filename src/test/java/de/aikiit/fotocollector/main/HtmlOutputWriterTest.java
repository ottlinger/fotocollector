package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanResult;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * @author hirsch
 * @version 2016-03-16, 22:25
 */
public class HtmlOutputWriterTest {

    @Test
    public void checkResult() {
        assertNull(new HtmlOutputWriter().write(new ScanResult()));
    }
}
