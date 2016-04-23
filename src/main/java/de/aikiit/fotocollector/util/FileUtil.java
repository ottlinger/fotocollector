package de.aikiit.fotocollector.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author hirsch
 * @version 2016-04-23, 23:59
 */
public final class FileUtil {

    /*
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
}
