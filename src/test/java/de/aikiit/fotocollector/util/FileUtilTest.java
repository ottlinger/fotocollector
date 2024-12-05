package de.aikiit.fotocollector.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

    @Test
    public void hashWithNullThenReturnDefault() {
        assertThat(FileUtil.getHash(null)).isEqualTo(FileUtil.NONE);
    }

    @Test
    public void hashWithErrorThenReturnDefault() {
        assertThat(FileUtil.getHash(new File("DoesNotExistDoesIt").toPath())).isEqualTo(FileUtil.NONE);
    }

    @Test
    public void compareHashAfterCalculationFromExternalFile() throws URISyntaxException {
        // hash differs on Windows!
        assertThat(FileUtil.getHash(Paths.get(ClassLoader.getSystemResource("hashme.txt").toURI()))).isEqualTo("5befc3514912917585421adfb4d030cc3f75d989d40494cd5ccd112e72bd1f4a");
    }
}
