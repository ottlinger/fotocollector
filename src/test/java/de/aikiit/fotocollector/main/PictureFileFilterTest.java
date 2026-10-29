package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.util.TestImages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PictureFileFilterTest {

    private final PictureFileFilter filter = new PictureFileFilter();

    @TempDir
    Path folder;

    @Test
    void rejectsNullDirectoriesAndMissingFiles() throws IOException {
        assertThat(filter.accept(null)).isFalse();
        assertThat(filter.accept(folder.toFile())).isFalse();
        assertThat(filter.accept(Files.createDirectory(folder.resolve("directory.jpg")).toFile())).isFalse();
        assertThat(filter.accept(folder.resolve("missing.png").toFile())).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "png, photo.png", "jpeg, photo.jPEg", "gif, photo.GIF",
            "bmp, photo.bmp", "tiff, photo.tiff",
            "png, extensionless", "jpeg, misleading.txt", "png, wrong.jpg"
    })
    void acceptsImageContentRegardlessOfFilename(String format, String name) throws IOException {
        Path image = TestImages.write(folder.resolve(name), format);

        assertThat(filter.accept(image.toFile())).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"fake.jpg", "fake.png", "fake.GIF", "fake.jpeg", "plain.txt", "extensionless"})
    void rejectsTextWithAnyExtension(String name) throws IOException {
        Path text = Files.writeString(folder.resolve(name), "This is text, not an image.");

        assertThat(filter.accept(text.toFile())).isFalse();
    }

    @Test
    void rejectsEmptyFileWithImageExtension() throws IOException {
        assertThat(filter.accept(Files.createFile(folder.resolve("empty.png")).toFile())).isFalse();
    }

    @Test
    void rejectsFileThatCannotBeOpened() {
        // Simulate a file disappearing after isFile(), without OS-dependent permission tests.
        File file = mock(File.class);
        when(file.isFile()).thenReturn(true);
        when(file.toPath()).thenReturn(folder.resolve("disappeared.png"));

        assertThat(filter.accept(file)).isFalse();
    }
}
