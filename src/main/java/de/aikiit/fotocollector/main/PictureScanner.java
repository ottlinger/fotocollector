package de.aikiit.fotocollector.main;

import com.google.common.collect.Lists;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.List;

/**
 * @author hirsch
 * @version 2016-03-03, 00:11
 */

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
        List<String> results = Lists.newArrayList();
        try {
            Files.walkFileTree(this.basePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() && PICTURE_MATCHER.accept(file.toFile())) {
                        results.add(file.toString());
                        System.out.println(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // intentional fallthrough
        }

        // FIXME
        return new ScanResult();
    }

    public ScanResult getFiles() {

        final ScanResult scanResult = new ScanResult();
        // TODO keep in sync with PictureFileFilter
        try {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(this.basePath, "*.{gif,jpg,png,jpeg}")) {
                for (Path path : files) {
                    final Path fileName = path.getFileName();
                    BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                    System.out.println(fileName + " created at " + attr.creationTime());
                    ScanEntry entry = new ScanEntry(fileName.toString(), new Date(attr.creationTime().toMillis()));
                    // TODO entry.setSize();
// TODO                    entry.setHashOverContent(DigestUtils.sha1(fileName.toString().getBytes(Charsets.UTF_8.name())));
                    scanResult.addEntry(entry);
                }
            }
        } catch (IOException e) {
            // intentional fallthrough
        }
        return scanResult;
    }


}
