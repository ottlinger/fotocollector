package de.aikiit.fotocollector.main;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author hirsch
 * @version 2016-03-07, 22:58
 */
public class PictureFileFilterTest {

    private static final PictureFileFilter FILE_FILTER = new PictureFileFilter();

    @Test
    public void isNullSafe() {
        assertThat(FILE_FILTER.accept(null)).isFalse();
    }

    @Test
    public void realFileNamesButNoPictures() {
        for (File f : Arrays.asList(new File("foo.txt"), new File("bar"), new File("."), new File(""))) {
            assertThat(FILE_FILTER.accept(f)).isFalse();
        }
    }

    @Test
    public void realFileNamesAndPictures() {
        for (File f : Arrays.asList(new File("foo.png"), new File("bar.GIF"), new File("email.jPEg"))) {
            assertThat(FILE_FILTER.accept(f)).isTrue();
        }
    }
}
