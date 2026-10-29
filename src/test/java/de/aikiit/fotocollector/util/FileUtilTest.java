package de.aikiit.fotocollector.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FileUtilTest {

    private static final String BASENAME = String.valueOf(System.currentTimeMillis());

    @TempDir
    private Path folder;

    @Test
    public void ensureUniqueness() throws IOException {
        assertThat(folder.resolve(BASENAME).toFile().createNewFile()).isTrue();

        String fileName = FileUtil.makeUnique(folder, BASENAME).toAbsolutePath().toString();
        assertThat(fileName).contains(BASENAME);
        assertThat(fileName).endsWith("_0");
    }

    @Test
    public void ensureUniquenessForMultipleSuffices() throws IOException {
        assertThat(folder.resolve(BASENAME).toFile().createNewFile()).isTrue();

        for (int no = 0; no < 4; no++) {
            assertThat(folder.resolve(BASENAME + "_" + no).toFile().createNewFile()).isTrue();
        }

        String fileName = FileUtil.makeUnique(folder, BASENAME).toAbsolutePath().toString();
        assertThat(fileName).contains(BASENAME);
        assertThat(fileName).endsWith("_4");
    }

    @ParameterizedTest
    @CsvSource({
            "photo.jpg, photo_0.jpg, photo_1.jpg",
            "archive.tar.gz, archive.tar_0.gz, archive.tar_1.gz",
            "fotocollector.json, fotocollector_0.json, fotocollector_1.json",
            "fotocollector.html, fotocollector_0.html, fotocollector_1.html",
            "README, README_0, README_1",
            ".hidden, .hidden_0, .hidden_1",
            ".hidden.jpg, .hidden_0.jpg, .hidden_1.jpg"
    })
    void insertsCounterBeforeExtension(String original, String first, String second) throws IOException {
        assertThat(FileUtil.makeUnique(folder, original)).isEqualTo(folder.resolve(original));
        Files.createFile(folder.resolve(original));

        assertThat(FileUtil.makeUnique(folder, original)).isEqualTo(folder.resolve(first));
        Files.createFile(folder.resolve(first));

        assertThat(FileUtil.makeUnique(folder, original)).isEqualTo(folder.resolve(second));
    }

    @Test
    public void hashWithNullThenReturnDefault() {
        assertThat(FileUtil.getHash(null)).isEqualTo(FileUtil.NONE);
    }

    @Test
    public void hashWithErrorThenReturnDefault() {
        assertThat(FileUtil.getHash(new File("DoesNotExistDoesIt").toPath())).isEqualTo(FileUtil.NONE);
    }

    /**
     * As hash differs under windows, we need a different expectation here.
     * @throws URISyntaxException
     * @see #compareHashAfterCalculationFromExternalFile()
     */
    @Test
    @EnabledOnOs({OS.WINDOWS})
    public void windowsOnlyCompareHashAfterCalculationFromExternalFile() throws URISyntaxException {
        assertThat(FileUtil.getHash(Paths.get(ClassLoader.getSystemResource("hashme.txt").toURI()))).isEqualTo("4416ceab285035770b55ab758f2429da68f8a5c72ff465c6216b819b389a51dd");
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    public void compareHashAfterCalculationFromExternalFile() throws URISyntaxException {
        assertThat(FileUtil.getHash(Paths.get(ClassLoader.getSystemResource("hashme.txt").toURI()))).isEqualTo("79830e4c358f80d3016d4365ed0ac801eaf4bdd65212d76b4c218e05321b0ad1");
    }

}
