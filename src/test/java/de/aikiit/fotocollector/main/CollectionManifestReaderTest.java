package de.aikiit.fotocollector.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIOException;

class CollectionManifestReaderTest {

    @TempDir
    Path temp;

    private final CollectionManifestReader reader = new CollectionManifestReader();

    @ParameterizedTest
    @ValueSource(strings = {
            "{not json", "null", "{}", "42", "[null]", "[{}]",
            "[{\"fileName\":\"a.jpg\",\"size\":-1}]",
            "[{\"fileName\":\"a.jpg\",\"size\":1.5}]",
            "[{\"fileName\":\"a.jpg\",\"size\":99999999999999999999999}]",
            "[{\"fileName\":\"a.jpg\",\"size\":1,\"hashOverContent\":\"bad\"}]",
            "[{\"fileName\":\"a.jpg\",\"size\":1,\"hashOverContent\":42}]",
            "[] {}"
    })
    void rejectsMalformedJsonReports(String content) throws IOException {
        Path input = Files.writeString(temp.resolve("bad.json"), content);

        assertThatIOException().isThrownBy(() -> reader.read(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"../outside.jpg", "/absolute.jpg", "..", ".", "", "  ", "C:outside.jpg"})
    void rejectsUnsafeFilenamesBeforeCreatingOutput(String name) throws IOException {
        Path input = Files.writeString(temp.resolve("bad.json"),
                "[{\"fileName\":\"" + name + "\",\"size\":0}]");
        Path output = temp.resolve("out");

        assertThatIOException().isThrownBy(() -> new FileCollector().collect(input, temp, output))
                .withMessageContaining("Invalid filename");
        assertThat(output).doesNotExist();
    }

    @Test
    void rejectsWindowsTraversal() throws IOException {
        Path input = Files.writeString(temp.resolve("bad.json"),
                "[{\"fileName\":\"..\\\\outside.jpg\",\"size\":0}]");

        assertThatIOException().isThrownBy(() -> reader.read(input))
                .withMessageContaining("Invalid filename");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<html>not a report</html>", "<table></table>",
            "<table><tr><th>Not a FotoCollector report</th></tr></table>",
            "<table><tr><th>Number</th><th>Filename</th><th>Size/Bytes</th><th>Hash (SHA-1)</th></tr></table>",
            "<table><tr><th>Number</th><th>Filename</th><th>Size/Bytes</th><th>Hash (SHA3-256)</th></tr><tr><td>broken</td></tr></table>",
            "<table><tr><th>Number</th><th>Filename</th><th>Size/Bytes</th><th>Hash (SHA3-256)</th></tr><tr><td>#1</td><td>a.jpg</td><td>no size</td><td>none</td></tr></table>"
    })
    void rejectsInvalidHtmlReports(String content) throws IOException {
        Path input = Files.writeString(temp.resolve("bad.html"), content);

        assertThatIOException().isThrownBy(() -> reader.read(input));
    }

    @Test
    void readsHtmlEntitiesAndDoesNotResolveExternalDoctype() throws IOException {
        Path input = Files.writeString(temp.resolve("report.HTML"), """
                <!DOCTYPE html SYSTEM "http://127.0.0.1:1/must-not-be-loaded">
                <table><tr><th>Number</th><th>Filename</th><th>Size/Bytes</th><th>Hash (SHA3-256)</th></tr>
                <tr><td>#1</td><td>photo &amp; &lt;holiday&gt;.jpg</td><td>0</td><td>none</td></tr></table>
                """);

        assertThat(reader.read(input)).containsExactly(
                new CollectionManifestReader.Entry("photo & <holiday>.jpg", 0, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"report_0.json", "report_12.JSON"})
    void readsReportsWithCollisionCounters(String name) throws IOException {
        Path input = Files.writeString(temp.resolve(name), "[]");

        assertThat(reader.read(input)).isEmpty();
    }

    @Test
    void normalizesUppercaseHashes() throws IOException {
        String hash = "AB".repeat(32);
        Path input = Files.writeString(temp.resolve("report.json"),
                "[{\"fileName\":\"a.jpg\",\"size\":0,\"hashOverContent\":\"" + hash + "\"}]");

        assertThat(reader.read(input).get(0).hash()).isEqualTo("ab".repeat(32));
    }

    @Test
    void rejectsMissingOrUnsupportedReport() throws IOException {
        assertThatIOException().isThrownBy(() -> reader.read(temp.resolve("missing.json")));
        Path input = Files.writeString(temp.resolve("report.txt"), "[]");
        assertThatIOException().isThrownBy(() -> reader.read(input))
                .withMessageContaining("Unsupported report format");
    }
}
