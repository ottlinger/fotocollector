package de.aikiit.fotocollector.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

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
}
