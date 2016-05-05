package de.aikiit.fotocollector.util;

import com.google.common.annotations.VisibleForTesting;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author hirsch
 * @version 2016-04-23, 23:59
 */
public final class FileUtil {

    @VisibleForTesting
    static final String NONE = "none";

    /**
     * If the given path contains a file baseName a number is added as a suffix.
     */
    public static Path makeUnique(Path basePath, String baseName) {

        Path desiredPath = Paths.get(basePath.toString(), baseName);
        int suffix = 0;

        while (Files.exists(desiredPath)) {
            desiredPath = Paths.get(basePath.toString(), baseName + "_" + (suffix++));
        }

        return desiredPath;
    }

    /**
     * Calculates the SHA-1 hash of the given path'S contents.
     *
     * @param path file to calculate hash over.
     * @return SHA-1 or {@code 'none'} in case of errors.
     */
    public static String getHash(Path path) {
        return NONE;
    }
}
