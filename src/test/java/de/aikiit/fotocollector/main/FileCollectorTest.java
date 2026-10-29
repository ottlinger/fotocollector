package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import de.aikiit.fotocollector.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class FileCollectorTest {

    @TempDir
    Path temp;

    private final FileCollector collector = new FileCollector();

    @ParameterizedTest
    @ValueSource(strings = {"json", "html"})
    void collectsMovedAndRenamedFilesRecursively(String format) throws IOException {
        Path original = Files.writeString(temp.resolve("photo & holiday ü.jpg"), "image contents");
        Path report = report(format, original);
        Path source = Files.createDirectories(temp.resolve("archive/nested"));
        Path moved = Files.move(original, source.resolve("renamed.dat"));
        Path output = temp.resolve("collected/new");

        FileCollector.Result result = collector.collect(report, source.getParent(), output);

        assertThat(result.missingFiles()).isEmpty();
        assertThat(result.copiedFiles()).containsExactly(output.resolve(original.getFileName()).toRealPath());
        assertThat(Files.readString(output.resolve(original.getFileName()))).isEqualTo("image contents");
        assertThat(Files.readString(moved)).isEqualTo("image contents");
    }

    @Test
    void doesNotMatchSameNameAndSizeWithDifferentContents() throws IOException {
        Path original = Files.writeString(temp.resolve("photo.jpg"), "right");
        Path report = report("json", original);
        Path source = Files.createDirectory(temp.resolve("archive"));
        Files.writeString(source.resolve("photo.jpg"), "wrong");

        FileCollector.Result result = collector.collect(report, source, temp.resolve("out"));

        assertThat(result.copiedFiles()).isEmpty();
        assertThat(result.missingFiles()).containsExactly("photo.jpg");
    }

    @Test
    void doesNotOverwriteExistingDestination() throws IOException {
        Path original = Files.writeString(temp.resolve("photo.jpg"), "new image");
        Path report = report("json", original);
        Path output = Files.createDirectory(temp.resolve("out"));
        Files.writeString(output.resolve("photo.jpg"), "keep this");

        FileCollector.Result result = collector.collect(report, temp, output);

        assertThat(result.missingFiles()).isEmpty();
        assertThat(Files.readString(output.resolve("photo.jpg"))).isEqualTo("keep this");
        assertThat(Files.readString(output.resolve("photo.jpg_0"))).isEqualTo("new image");
    }

    @Test
    void ignoresOutputSubtreeWhileSearching() throws IOException {
        Path original = Files.writeString(temp.resolve("photo.jpg"), "image");
        Path report = report("json", original);
        Path source = Files.createDirectory(temp.resolve("archive"));
        Path output = Files.createDirectory(source.resolve("out"));
        Files.move(original, output.resolve("photo.jpg"));

        FileCollector.Result result = collector.collect(report, source, output);

        assertThat(result.copiedFiles()).isEmpty();
        assertThat(result.missingFiles()).containsExactly("photo.jpg");
        assertThat(output.resolve("photo.jpg_0")).doesNotExist();
    }

    @Test
    void copiesEachRequestedEntryOnlyOnceEvenWithDuplicateSources() throws IOException {
        Path original = Files.writeString(temp.resolve("photo.jpg"), "image");
        Path report = report("json", original);
        Files.copy(original, temp.resolve("duplicate.jpg"));

        FileCollector.Result result = collector.collect(report, temp, temp.resolve("out"));

        assertThat(result.copiedFiles()).hasSize(1);
        assertThat(result.missingFiles()).isEmpty();
    }

    @Test
    void recreatesDifferentRequestedNamesWithIdenticalContents() throws IOException {
        Path first = Files.writeString(temp.resolve("first.jpg"), "image");
        Path second = Files.writeString(temp.resolve("second.jpg"), "image");
        Path report = report("json", first, second);
        Files.delete(second);

        FileCollector.Result result = collector.collect(report, temp, temp.resolve("out"));

        assertThat(result.copiedFiles()).hasSize(2);
        assertThat(result.missingFiles()).isEmpty();
        assertThat(Files.readString(temp.resolve("out/second.jpg"))).isEqualTo("image");
    }

    @Test
    void reportsMissingFilesWhileCopyingAvailableFiles() throws IOException {
        Path found = Files.writeString(temp.resolve("found.jpg"), "found");
        Path missing = Files.writeString(temp.resolve("missing.jpg"), "missing");
        Path report = report("html", found, missing);
        Files.delete(missing);

        FileCollector.Result result = collector.collect(report, temp, temp.resolve("out"));

        assertThat(result.copiedFiles()).hasSize(1);
        assertThat(result.missingFiles()).containsExactly("missing.jpg");
    }

    @Test
    void fallsBackToNameAndSizeWhenHashIsUnavailable() throws IOException {
        Path input = Files.writeString(temp.resolve("report.json"),
                "[{\"fileName\":\"photo.jpg\",\"size\":5,\"hashOverContent\":\"none\"}]");
        Path source = Files.createDirectory(temp.resolve("archive"));
        Files.writeString(source.resolve("photo.jpg"), "image");

        FileCollector.Result result = collector.collect(input, source, temp.resolve("out"));

        assertThat(result.copiedFiles()).hasSize(1);
        assertThat(result.missingFiles()).isEmpty();
    }

    @Test
    void fallbackRequiresBothFilenameAndSize() throws IOException {
        Path input = Files.writeString(temp.resolve("report.json"),
                "[{\"fileName\":\"photo.jpg\",\"size\":5}]");
        Path source = Files.createDirectory(temp.resolve("archive"));
        Files.writeString(source.resolve("photo.jpg"), "wrong size");
        Files.writeString(source.resolve("other.jpg"), "image");

        FileCollector.Result result = collector.collect(input, source, temp.resolve("out"));

        assertThat(result.copiedFiles()).isEmpty();
        assertThat(result.missingFiles()).containsExactly("photo.jpg");
    }

    @Test
    void doesNotFollowSymbolicFilesOrDirectories() throws IOException {
        Path original = Files.writeString(temp.resolve("photo.jpg"), "image");
        Path report = report("json", original);
        Path outside = Files.createDirectory(temp.resolve("outside"));
        Files.move(original, outside.resolve("photo.jpg"));
        Path source = Files.createDirectory(temp.resolve("archive"));
        try {
            Files.createSymbolicLink(source.resolve("photo.jpg"), outside.resolve("photo.jpg"));
            Files.createSymbolicLink(source.resolve("directory"), outside);
        } catch (IOException | UnsupportedOperationException | SecurityException e) {
            assumeTrue(false, "Symbolic links are unavailable: " + e.getMessage());
        }

        FileCollector.Result result = collector.collect(report, source, temp.resolve("out"));

        assertThat(result.copiedFiles()).isEmpty();
        assertThat(result.missingFiles()).containsExactly("photo.jpg");
    }

    @Test
    void acceptsEmptyAndObjectShapedJsonReports() throws IOException {
        Path input = Files.writeString(temp.resolve("report.json"), "{\"entries\":[]}");

        FileCollector.Result result = collector.collect(input, temp, temp.resolve("out"));

        assertThat(result.copiedFiles()).isEmpty();
        assertThat(result.missingFiles()).isEmpty();
    }

    @Test
    void rejectsOutputThatContainsTheSourceFolder() throws IOException {
        Path input = Files.writeString(temp.resolve("report.json"), "[]");
        Path source = Files.createDirectory(temp.resolve("archive"));

        assertThatIOException().isThrownBy(() -> collector.collect(input, source, source))
                .withMessageContaining("must not equal or contain");
        assertThatIOException().isThrownBy(() -> collector.collect(input, source, temp))
                .withMessageContaining("must not equal or contain");
    }

    @Test
    void rejectsMissingSourceFolder() throws IOException {
        Path input = Files.writeString(temp.resolve("report.json"), "[]");

        assertThatIOException().isThrownBy(() -> collector.collect(input, temp.resolve("missing"), temp.resolve("out")));
        assertThat(temp.resolve("out")).doesNotExist();
    }

    @Test
    void rejectsSourceThatIsNotADirectory() throws IOException {
        Path input = Files.writeString(temp.resolve("report.json"), "[]");

        assertThatIOException().isThrownBy(() -> collector.collect(input, input, temp.resolve("out")))
                .withMessageContaining("not a directory");
    }

    private Path report(String format, Path... files) throws IOException {
        ScanResult scan = new ScanResult();
        for (Path file : files) {
            ScanEntry entry = new ScanEntry(file.getFileName().toString(), new Date(0));
            entry.setSize(Files.size(file));
            entry.setHashOverContent(FileUtil.getHash(file));
            scan.addEntry(entry);
        }
        OutputResult report = format.equals("json") ? new JsonOutputWriter().write(scan)
                : new HtmlOutputWriter().write(scan);
        return Files.writeString(temp.resolve("report." + format), report.result());
    }
}
