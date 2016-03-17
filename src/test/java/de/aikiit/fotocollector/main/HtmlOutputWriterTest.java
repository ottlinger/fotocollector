package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.ScanResult;
import org.junit.Test;

import java.util.Arrays;

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
            assertThat(result.getResult()).contains("empty");
        }
    }
}
