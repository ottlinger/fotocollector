package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import de.aikiit.fotocollector.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * @author hirsch
 * @version 2016-03-03, 00:11
 */
@Slf4j
public final class PictureScanner {

    private static final PictureFileFilter PICTURE_MATCHER = new PictureFileFilter();

    private final Path basePath;

    public PictureScanner(Path baseDir) {
        if (baseDir == null) {
            this.basePath = Paths.get(System.getProperty("user.home")).resolve("Documents/Pictures");
        } else {
            this.basePath = baseDir;
        }
    }

    // TODO add recursive file scanning as an option
    // http://www.ntu.edu.sg/home/ehchua/programming/java/J5b_IO.html
    public ScanResult getFilesRecursively() {

        final ScanResult scanResult = new ScanResult();
        try {
            Files.walkFileTree(this.basePath, new SimpleFileVisitor<Path>() {
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

        return new ScanResult();
    }

    private ScanEntry convert(Path path) throws IOException {
        final Path fileName = path.getFileName();
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        ScanEntry entry = new ScanEntry(fileName.toString(), new Date(attr.creationTime().toMillis()));
        entry.setSize(Files.size(path));
        entry.setHashOverContent(FileUtil.getHash(path));
        return entry;
    }

    public ScanResult getFiles() {

        final ScanResult scanResult = new ScanResult();
        try {
            // TODO keep in sync with PictureFileFilter
            try (DirectoryStream<Path> files = Files.newDirectoryStream(this.basePath, "*.{gif,jpg,png,jpeg}")) {
                for (Path path : files) {
                    scanResult.addEntry(convert(path));
                }
            }
        } catch (IOException e) {
            // intentional fallthrough
        }
        return scanResult;
    }

}
