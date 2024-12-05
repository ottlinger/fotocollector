package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.Files.walk;

/**
 * Finds/traverses from a given base path in order to find files.
 *
 * @author hirsch
 * @version 2016-05-20, 22:38
 */
@Slf4j
public class FileFinder {

    public static void main(String... args) throws IOException {

        // https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#find-java.nio.file.Path-int-java.util.function.BiPredicate-java.nio.file.FileVisitOption...-
        final URI path = args == null || args.length < 1 ? Paths.get("/tmp/").toUri() : Paths.get(args[0]).toUri();
        try {
            walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .forEach(e -> log.info(e.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void scan(ScanResult result) throws IOException {
        if (result == null || result.isEmpty()) {
            log.info("Nothing to be done here.");
        } else {
            for (ScanEntry entry : result.getEntries()) {
                main(entry.getFileName());
            }
        }
    }
}
