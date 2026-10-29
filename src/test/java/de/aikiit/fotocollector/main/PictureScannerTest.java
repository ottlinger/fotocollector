package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ProgressUpdate;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import de.aikiit.fotocollector.util.FileUtil;
import de.aikiit.fotocollector.util.TestImages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static de.aikiit.fotocollector.ProgressUpdate.Operation.SCAN;
import static de.aikiit.fotocollector.ProgressUpdate.State.*;

import static org.assertj.core.api.Assertions.assertThat;

class PictureScannerTest {

    @TempDir
    Path folder;

    @Test
    void emptyDirectoryHasNoPictures() {
        PictureScanner scanner = new PictureScanner(folder);

        assertThat(scanner.getFiles().isEmpty()).isTrue();
        assertThat(scanner.getFilesRecursively().isEmpty()).isTrue();
    }

    @Test
    void missingDirectoryHasNoPictures() {
        PictureScanner scanner = new PictureScanner(folder.resolve("missing"));

        assertThat(scanner.getFiles().isEmpty()).isTrue();
        assertThat(scanner.getFilesRecursively().isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void reportsCheckedFilesAndFoundImagesSeparately(boolean recursive) throws IOException {
        TestImages.write(folder.resolve("image"));
        Files.writeString(folder.resolve("fake.jpg"), "not an image");
        TestImages.write(Files.createDirectory(folder.resolve("nested")).resolve("image.png"));
        List<ProgressUpdate> updates = new ArrayList<>();
        PictureScanner scanner = new PictureScanner(folder, updates::add);

        if (recursive) {
            scanner.getFilesRecursively();
        } else {
            scanner.getFiles();
        }

        long total = recursive ? 3 : 2;
        assertThat(updates.get(0)).isEqualTo(new ProgressUpdate(SCAN, 0, -1, 0, 0, RUNNING));
        assertThat(updates.get(1)).isEqualTo(new ProgressUpdate(SCAN, 0, total, 0, 0, RUNNING));
        assertThat(updates.get(updates.size() - 1)).isEqualTo(
                new ProgressUpdate(SCAN, total, total, recursive ? 2 : 1, 0, COMPLETED));
        assertThat(updates).extracting(ProgressUpdate::current).isSorted();
        assertThat(updates).extracting(ProgressUpdate::found).isSorted();
    }

    @Test
    void reportsEmptyScanAndFailedScanWithoutPretendingSuccess() {
        List<ProgressUpdate> updates = new ArrayList<>();
        new PictureScanner(folder, updates::add).getFiles();
        assertThat(updates.get(updates.size() - 1)).isEqualTo(
                new ProgressUpdate(SCAN, 0, 0, 0, 0, COMPLETED));

        updates.clear();
        new PictureScanner(folder.resolve("missing"), updates::add).getFiles();
        assertThat(updates.get(updates.size() - 1).state()).isEqualTo(FAILED);
    }

    @Test
    void withDefaultParam() {
        assertThat(new PictureScanner(null)).isNotNull();
    }

    @Test
    void scansImageContentInsteadOfExtensionsAndPreservesMetadata() throws IOException {
        Path image = TestImages.write(folder.resolve("extensionless"));
        TestImages.write(folder.resolve("renamed.txt"), "jpeg");
        TestImages.write(folder.resolve("upper.PNG"));
        TestImages.write(folder.resolve("photo.bmp"), "bmp");
        Files.writeString(folder.resolve("fake.jpg"), "not an image");
        Files.createFile(folder.resolve("empty.png"));
        Path nested = Files.createDirectory(folder.resolve("directory.jpg"));
        TestImages.write(nested.resolve("nested.png"));

        ScanResult result = new PictureScanner(folder).getFiles();

        assertThat(result.getEntries()).extracting(ScanEntry::fileName)
                .containsExactly("extensionless", "photo.bmp", "renamed.txt", "upper.PNG");
        ScanEntry entry = result.getEntries().iterator().next();
        assertThat(entry.size()).isEqualTo(Files.size(image));
        assertThat(entry.hashOverContent()).isEqualTo(FileUtil.getHash(image));
        assertThat(entry.creationDate()).isNotNull();
    }

    @Test
    void recursiveScanReturnsImagesAtAllDepthsUsingTheSameContentFilter() throws IOException {
        TestImages.write(folder.resolve("root.dat"));
        Path nested = Files.createDirectories(folder.resolve("nested/deeper"));
        TestImages.write(nested.resolve("extensionless"), "gif");
        TestImages.write(nested.resolve("photo.tiff"), "tiff");
        Files.writeString(nested.resolve("fake.png"), "not an image");
        Files.createFile(nested.resolve("empty.jpg"));

        ScanResult result = new PictureScanner(folder).getFilesRecursively();

        assertThat(result.getEntries()).extracting(ScanEntry::fileName)
                .containsExactly("extensionless", "photo.tiff", "root.dat");
    }
}
