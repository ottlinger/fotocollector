package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author hirsch
 * @version 2016-02-22, 20:06
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonOutputWriterTest {

    private final JsonOutputWriter writer = new JsonOutputWriter();
    private final static Path cwd = new File(".").toPath();

    @Test
    public void writeWithoutResults() throws IOException {
        final ScanResult result = new ScanResult();
        final OutputResult outputResult = writer.write(result);
        assertTrue(outputResult.isEmpty());
        assertFalse(outputResult.flush(cwd).isPresent());
    }

    @Test
    public void writeWithSingleResult() throws IOException {
        final String fileName = "testFileName.txt";
        final ScanResult result = new ScanResult();
        final ScanEntry entry = new ScanEntry(fileName, new Date(1));
        result.addEntry(entry);
        result.addEntry(new ScanEntry("a" + fileName, new Date(0)));
        final OutputResult outputResult = writer.write(result);
        assertFalse(outputResult.isEmpty());

        final String json = outputResult.getResult();
        System.out.println(json);
        assertEquals("[{\"fileName\":\"testFileName.txt\",\"creationDate\":\"1970-01-01 00:00:00\"},{\"fileName\":\"atestFileName.txt\",\"creationDate\":\"1970-01-01 00:00:00\"}]", json);

        assertTrue(outputResult.flush(cwd).isPresent());
        assertTrue(new File(".", "fotocollector.json").delete());
    }

}
