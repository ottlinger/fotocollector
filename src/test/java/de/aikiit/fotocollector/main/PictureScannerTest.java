package de.aikiit.fotocollector.main;

import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author hirsch
 * @version 2016-03-03, 00:17
 */
public class PictureScannerTest {
    @Test
    public void withParamNoPicturesInTemp() {
        assertTrue(new PictureScanner(Paths.get("/tmp")).getFiles().isEmpty());
    }

    @Test
    public void withDefaultParam() {
        List<String> pictures = new PictureScanner(null).getFiles();
        assertNotNull(pictures);
    }
    @Test
    public void scanRecursively() {
        List<String> pictures = new PictureScanner(Paths.get(System.getProperty("user.home")).resolve("Documents/Pictures/2016")).getFilesRecursively();
        assertNotNull(pictures);
    }
}
