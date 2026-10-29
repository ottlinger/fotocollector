package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import de.aikiit.fotocollector.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Objects;

/**
 * @author hirsch
 * @version 2016-03-03, 00:11
 */
@Slf4j
public final class PictureScanner {

    private static final PictureFileFilter PICTURE_MATCHER = new PictureFileFilter();

    private final Path basePath;

    public PictureScanner(Path baseDir) {
        this.basePath = Objects.requireNonNullElseGet(baseDir, () -> Paths.get(System.getProperty("user.home")).resolve("Documents/Pictures"));
    }

    /**
     * Recursively finds images by content, without restricting filename extensions.
     * Directory symbolic links are not followed.
     */
    public ScanResult getFilesRecursively() {

        final ScanResult scanResult = new ScanResult();
        try {
            Files.walkFileTree(this.basePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() && PICTURE_MATCHER.accept(file.toFile())) {
                        scanResult.addEntry(convert(file));
                        log.info("{} created at {}", file.toFile().getName(), attrs.creationTime());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // intentional fallthrough
        }

        return scanResult;
    }

    private ScanEntry convert(Path path) throws IOException {
        final Path fileName = path.getFileName();
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        return new ScanEntry(fileName.toString(), FileUtil.getHash(path),
                new Date(attr.creationTime().toMillis()), Files.size(path));
    }

    /**
     * Finds images directly in the base directory using the same content filter as
     * the recursive scan. All directory entries must be considered, including files
     * without extensions; a filename glob would discard valid images before detection.
     */
    public ScanResult getFiles() {

        final ScanResult scanResult = new ScanResult();
        try {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(this.basePath)) {
                for (Path path : files) {
                    if (PICTURE_MATCHER.accept(path.toFile())) {
                        scanResult.addEntry(convert(path));
                    }
                }
            }
        } catch (IOException e) {
            // intentional fallthrough
        }
        return scanResult;
    }

}
