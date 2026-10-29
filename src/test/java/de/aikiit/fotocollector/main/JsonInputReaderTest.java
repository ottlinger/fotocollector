package de.aikiit.fotocollector.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author hirsch
 * @version 2016-05-25, 23:25
 */
public class JsonInputReaderTest {

    @TempDir
    Path tempDir;

    @Test
    public void readEmptyScanResult() throws IOException {
        final Path input = Files.writeString(tempDir.resolve("empty.json"), "{\"entries\":[]}");

        assertThat(new JsonInputReader().read(input).getEntries()).isEmpty();
    }

    @Test
    public void malformedJsonYieldsEmptyResultAndNoException() throws IOException {
        final Path input = Files.writeString(tempDir.resolve("malformed.json"), "{not valid JSON");

        assertThat(new JsonInputReader().read(input).isEmpty()).isTrue();
    }

    @Test
    public void incompatibleJsonYieldsEmptyResultAndNoException() throws IOException {
        final Path input = Files.writeString(tempDir.resolve("incompatible.json"), "{\"entries\":42}");

        assertThat(new JsonInputReader().read(input).isEmpty()).isTrue();
    }

    @Test
    public void nullParameterYieldsEmptyResult() {
        assertThat(new JsonInputReader().read(null).isEmpty()).isTrue();
    }

    @Test
    public void illegalPathYieldsEmptyResultAndNoException() {
        assertThat(new JsonInputReader().read(new File("FILEDOESnotExRight" + System.currentTimeMillis()).toPath()).isEmpty()).isTrue();
    }

}
