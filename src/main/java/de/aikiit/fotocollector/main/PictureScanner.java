package de.aikiit.fotocollector.main;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * @author hirsch
 * @version 2016-03-03, 00:11
 */

final class PictureScanner {

    private static final PictureFileFilter PICTURE_MATCHER = new PictureFileFilter();

    private final Path basePath;

    public PictureScanner(Path baseDir) {
        if (baseDir == null) {
            this.basePath = Paths.get(System.getProperty("user.home")).resolve("Documents/Pictures");
        } else {
            this.basePath = baseDir;
        }
    }

    // TODO add recursive file scanning by option
    public List<String> getFilesRecursively() {
        List<String> results = Lists.newArrayList();
        try {
            Files.walkFileTree(this.basePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // TODO extraxt picture recognition and case insensitivity

                    if (!attrs.isDirectory() && file.toString().endsWith("jpg")) {
                        results.add(file.toString());
                        System.out.println(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // intentional fallthrough
        }

        return results;
    }

    public List<String> getFiles() {

        List<String> results = Lists.newArrayList();
        try {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(this.basePath, "*.{gif,jpg,png}")) {
                for (Path path : files) {
                    final Path fileName = path.getFileName();
                    System.out.println(fileName);
                    results.add(fileName.toString());
                }
            }
        } catch (IOException e) {
            // intentional fallthrough
        }
        return results;
    }


}