package de.aikiit.fotocollector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author hirsch
 * @version 2016-04-28, 23:13
 */
public class FotoCollectorApplicationTest {

    @TempDir
    private Path folder;

    @Test
    public void launchWithArgsThenAllOutputFilesAreWritten() throws IOException {
        // create fake image
        String name = UUID.randomUUID().toString() + ".png";
        assertThat(folder.resolve(name).toFile().createNewFile()).isTrue();

        FotoCollectorApplication.main(new String[] {folder.toFile().toString()});
        for(String fileName : Arrays.asList("fotocollector.html", "fotocollector.json")) {
            final File output = folder.resolve(fileName).toFile();
            assertThat(output).exists();
        }
    }

    @Test
    public void launchWithoutArgsThenNoOutputFilesAreWritten() throws IOException {
        FotoCollectorApplication.main(null);

        for(String fileName : Arrays.asList("fotocollector.html", "fotocollector.json")) {
            final File output = new File(".", fileName);
            assertThat(output).doesNotExist();
        }
    }
}
