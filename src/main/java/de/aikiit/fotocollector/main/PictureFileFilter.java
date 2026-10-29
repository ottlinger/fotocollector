package de.aikiit.fotocollector.main;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Accepts regular files whose content Tika identifies with an {@code image/*} MIME type.
 * Filenames are deliberately not passed to Tika: an extension must neither make a
 * non-image pass nor prevent an extensionless or renamed image from being found.
 * Detection identifies a format; it does not validate or fully decode the image.
 *
 * @author hirsch
 * @version 2016-03-07, 22:50
 */
@Slf4j
class PictureFileFilter implements FileFilter {

    private final Tika tika = new Tika();

    @Override
    public boolean accept(final File pathname) {
        if (pathname == null || !pathname.isFile()) {
            return false;
        }

        try (InputStream input = new BufferedInputStream(Files.newInputStream(pathname.toPath()))) {
            return tika.detect(input).startsWith("image/");
        } catch (IOException e) {
            log.debug("Skipping file whose content cannot be read: {}", pathname, e);
            return false;
        }
    }
}
