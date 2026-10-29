package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author hirsch
 * @version 2016-05-23, 00:06
 */
public class FileFinderTest {
    @TempDir
    Path folder;

    @Test
    public void emptyResultAndNullParameters() throws IOException {
        FileFinder.scan(null);
        FileFinder.scan(new ScanResult());
    }

    @Test
    public void scanForCreatedResult() throws IOException {
        Path directory = Files.createDirectory(folder.resolve("collection"));
        Path nested = Files.createDirectories(directory.resolve("nested"));
        Files.createDirectory(directory.resolve("empty"));
        Path image = Files.writeString(directory.resolve("photo.jpg"), "image");
        Path nestedImage = Files.writeString(nested.resolve("nested.png"), "nested image");
        Path standaloneImage = Files.writeString(folder.resolve("standalone.jpg"), "another image");
        ScanResult result = new ScanResult();
        result.addEntry(new ScanEntry(directory.toString()));
        result.addEntry(new ScanEntry(standaloneImage.toString()));

        StringWriter output = new StringWriter();
        WriterAppender appender = new WriterAppender(new PatternLayout("%m%n"), output);
        Logger logger = Logger.getLogger(FileFinder.class);
        logger.addAppender(appender);
        try {
            FileFinder.scan(result);

            assertThat(output.toString().lines()).containsExactlyInAnyOrder(
                    image.toString(), nestedImage.toString(), standaloneImage.toString());
        } finally {
            logger.removeAppender(appender);
            appender.close();
        }
    }
}
