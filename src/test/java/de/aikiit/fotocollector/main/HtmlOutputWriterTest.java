package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author hirsch
 * @version 2016-03-16, 22:25
 */
public class HtmlOutputWriterTest {

    @Test
    public void checkEmptyResult() {
        for (ScanResult input : Arrays.asList(null, new ScanResult())) {
            final OutputResult result = new HtmlOutputWriter().write(input);
            assertThat(result.isEmpty());
        }
    }

    @Test
    public void checkWithEntries() {
        final ScanResult input = new ScanResult();
        final ScanEntry image = new ScanEntry("ASplendidPic.jpeg", new Date(12345));
        image.setSize(-1l);
        input.addEntry(image);
        final OutputResult write = new HtmlOutputWriter().write(input);
        assertThat(write.getResult()).isNotEmpty();
        System.out.println(write.getResult());
    }
}
