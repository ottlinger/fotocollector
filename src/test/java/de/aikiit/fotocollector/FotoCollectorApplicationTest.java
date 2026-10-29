package de.aikiit.fotocollector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.UUID;

import de.aikiit.fotocollector.util.TestImages;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIOException;
/**
 * @author hirsch
 * @version 2016-04-28, 23:13
 */
public class FotoCollectorApplicationTest {

    @TempDir
    private Path folder;

    @Test
    public void launchWithArgsThenAllOutputFilesAreWritten() throws IOException {
        // Reports must be generated from actual image content, even without an extension.
        String name = UUID.randomUUID().toString();
        TestImages.write(folder.resolve(name));
        Files.writeString(folder.resolve("fake.jpg"), "not an image");

        FotoCollectorApplication.main(new String[] {folder.toFile().toString()});
        for(String fileName : Arrays.asList("fotocollector.html", "fotocollector.json")) {
            final File output = folder.resolve(fileName).toFile();
            assertThat(output).exists();
            assertThat(Files.readString(output.toPath())).contains(name).doesNotContain("fake.jpg");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"json", "html"})
    void collectFromGeneratedReport(String format) throws IOException {
        Path original = TestImages.write(folder.resolve("photo.jpg"), "jpeg");
        byte[] imageContent = Files.readAllBytes(original);
        FotoCollectorApplication.main(new String[] {folder.toString()});
        Path archive = Files.createDirectories(folder.resolve("archive/nested"));
        Files.move(original, archive.resolve("renamed.jpg"));
        Path output = folder.resolve("collected");

        FotoCollectorApplication.main(new String[] {"collect", "--input",
                folder.resolve("fotocollector." + format).toString(), "--start",
                folder.resolve("archive").toString(), "--output", output.toString()});

        assertThat(Files.readAllBytes(output.resolve("photo.jpg"))).isEqualTo(imageContent);
        assertThat(archive.resolve("renamed.jpg")).exists();
    }

    @Test
    void collectReportsMissingFilesAsFailure() throws IOException {
        Path report = Files.writeString(folder.resolve("report.json"),
                "[{\"fileName\":\"missing.jpg\",\"size\":99}]");

        assertThatIOException().isThrownBy(() -> FotoCollectorApplication.main(new String[] {
                "collect", "--input", report.toString(), "--start", folder.toString(),
                "--output", folder.resolve("out").toString()}))
                .withMessageContaining("Files not found: missing.jpg");
    }

    @Test
    void collectRejectsMissingUnknownAndDuplicateArguments() {
        for (String[] args : new String[][] {
                {"collect"}, {"collect", "--input"}, {"collect", "--unknown", "value"},
                {"collect", "--input", "a", "--input", "b"},
                {"collect", "--input", "--start"}, {"collect", "--input", ""}
        }) {
            assertThatIllegalArgumentException().isThrownBy(() -> FotoCollectorApplication.main(args))
                    .withMessageContaining("Usage:");
        }
        assertThat(folder.resolve("out")).doesNotExist();
    }

    @Test
    void helpDoesNotScanOrWriteReports() throws IOException {
        FotoCollectorApplication.main(new String[] {"--help"});
        FotoCollectorApplication.main(new String[] {"collect", "--help"});

        assertThat(folder.resolve("fotocollector.json")).doesNotExist();
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
