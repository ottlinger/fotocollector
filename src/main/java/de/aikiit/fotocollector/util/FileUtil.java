package de.aikiit.fotocollector.util;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author hirsch
 * @version 2016-04-23, 23:59
 */
public final class FileUtil {

    @VisibleForTesting
    static final String NONE = "none";

    /**
     * The currently used hashing algorithm.
     */
    public static final String HASH_ALGORITHM = "SHA3-256";

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
     * Calculates the {@value #HASH_ALGORITHM} hash of the given path's contents.
     * Unfortunately the current implementation is OS-specific.
     *
     * @param path file to calculate hash over.
     * @return {@value #HASH_ALGORITHM} or {@code 'none'} in case of errors.
     */
    public static String getHash(Path path) {
        try {
            return new DigestUtils(HASH_ALGORITHM).digestAsHex((Files.newInputStream(path, StandardOpenOption.READ)));
        } catch (NullPointerException | IOException e) {
            return NONE;
        }
    }
}
