package de.aikiit.fotocollector.main;

import com.google.common.collect.Lists;
import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import org.junit.jupiter.api.Test;

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
            assertThat(result.isEmpty()).isTrue();
        }
    }

    @Test
    public void checkWithEntries() {
        final ScanResult input = new ScanResult();
        final String fileName = "ASplendidPic.jpeg";
        final ScanEntry image = new ScanEntry(fileName, new Date(12345));
        image.setSize(-1L);
        input.addEntry(image);
        final OutputResult write = new HtmlOutputWriter().write(input);
        final String writeResult = write.result();
        assertThat(writeResult).isNotEmpty();

        for (String token : Lists.newArrayList("T", "-1", fileName, "<tr><td>#1</td>", "<tr><th>Number</th><th>Filename</th><th>Size/Bytes</th><th>Hash (SHA-1)</th></tr>")) {
            assertThat(writeResult).contains(token);
        }
        System.out.println(writeResult);
    }
}
