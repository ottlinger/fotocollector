package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanResult;
import org.junit.Test;

import java.nio.file.Paths;

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
        ScanResult pictures = new PictureScanner(null).getFiles();
        assertNotNull(pictures);
    }
    @Test
    public void scanRecursively() {
        ScanResult pictures = new PictureScanner(Paths.get(System.getProperty("user.home")).resolve("Documents/Pictures/2016")).getFilesRecursively();
        assertNotNull(pictures);
    }
}
