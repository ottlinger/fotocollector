package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author hirsch
 * @version 2016-03-03, 00:17
 */
public class PictureScannerTest {
    @Test
    public void withParamNoPicturesInTemp() {
        assertThat(new PictureScanner(Paths.get("/tmp")).getFiles().isEmpty()).isTrue();
    }

    @Test
    public void withDefaultParam() {
        ScanResult pictures = new PictureScanner(null).getFiles();
        assertThat(pictures).isNotNull();
    }

    @Test
    public void scanRecursively() {
        ScanResult pictures = new PictureScanner(Paths.get(System.getProperty("user.home")).resolve("Documents/Pictures/2016")).getFilesRecursively();
        assertThat(pictures).isNotNull();
    }
}
