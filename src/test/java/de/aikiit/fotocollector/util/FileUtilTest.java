package de.aikiit.fotocollector.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author hirsch
 * @version 2016-04-24, 00:07
 */
public class FileUtilTest {

    private static final String BASENAME = String.valueOf(System.currentTimeMillis());

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void ensureUniqueness() throws IOException {
        File f = folder.newFile(BASENAME);
        assertTrue(f.exists());

        String fileName = FileUtil.makeUnique(folder.getRoot().toPath(), BASENAME).toAbsolutePath().toString();
        assertTrue(fileName.contains(BASENAME));
        assertTrue(fileName.endsWith("_0"));
    }

    @Test
    public void ensureUniquenessForMultipleSuffices() throws IOException {
        File f = folder.newFile(BASENAME);
        assertTrue(f.exists());

        for (int no = 0; no < 4; no++) {
            assertTrue(folder.newFile(BASENAME + "_" + no).exists());
        }

        String fileName = FileUtil.makeUnique(folder.getRoot().toPath(), BASENAME).toAbsolutePath().toString();
        assertTrue(fileName.contains(BASENAME));
        assertTrue(fileName.endsWith("_4"));
    }

    @Test
    public void hashWithNull_returnDefault() {
        assertThat(FileUtil.getHash(null)).isEqualTo(FileUtil.NONE);
    }

    @Test
    public void hashWithError_returnDefault() {
        assertThat(FileUtil.getHash(new File("DoesNotExistDoesIt").toPath())).isEqualTo(FileUtil.NONE);
    }

    @Test
    public void compareHashAfterCalculationFromExternalFile() throws URISyntaxException {
        assertThat(FileUtil.getHash(Paths.get(ClassLoader.getSystemResource("hashme.txt").toURI()))).isEqualTo("5befc3514912917585421adfb4d030cc3f75d989d40494cd5ccd112e72bd1f4a");
    }
}
