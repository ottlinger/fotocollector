package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ProgressListener;
import de.aikiit.fotocollector.ProgressUpdate;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;
import de.aikiit.fotocollector.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import static de.aikiit.fotocollector.ProgressUpdate.Operation.SCAN;
import static de.aikiit.fotocollector.ProgressUpdate.State.*;

/**
 * @author hirsch
 * @version 2016-03-03, 00:11
 */
@Slf4j
public final class PictureScanner {

    private static final PictureFileFilter PICTURE_MATCHER = new PictureFileFilter();

    private final Path basePath;
    private final ProgressListener progress;

    public PictureScanner(Path baseDir) {
        this(baseDir, ProgressListener.NONE);
    }

    public PictureScanner(Path baseDir, ProgressListener progress) {
        this.basePath = Objects.requireNonNullElseGet(baseDir, () -> Paths.get(System.getProperty("user.home")).resolve("Documents/Pictures"));
        this.progress = Objects.requireNonNull(progress);
    }

    /**
     * Recursively finds images by content, without restricting filename extensions.
     * Directory symbolic links are not followed.
     */
    public ScanResult getFilesRecursively() {

        return scan(Integer.MAX_VALUE);
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

        return scan(1);
    }

    /**
     * Count directory entries first, without Tika or hashing, then process them.
     * Neither pass follows symbolic links. Keep only counters, not a list of paths,
     * so large recursive scans do not require memory proportional to file count.
     */
    private ScanResult scan(int depth) {
        ScanResult result = new ScanResult();
        long[] total = {-1};
        long[] checked = {0};
        long[] found = {0};
        boolean completed = false;
        progress.onProgress(new ProgressUpdate(SCAN, 0, -1, 0, 0, RUNNING));
        try {
            long[] count = {0};
            visitFiles(depth, path -> count[0]++);
            total[0] = count[0];
            progress.onProgress(new ProgressUpdate(SCAN, 0, total[0], 0, 0, RUNNING));
            visitFiles(depth, path -> {
                if (PICTURE_MATCHER.accept(path.toFile())) {
                    result.addEntry(convert(path));
                    found[0]++;
                }
                checked[0]++;
                progress.onProgress(new ProgressUpdate(SCAN, checked[0], total[0], found[0], 0, RUNNING));
            });
            completed = true;
        } catch (IOException e) {
            log.warn("Cannot finish scan of {}: {}", basePath, e.getMessage());
        } finally {
            progress.onProgress(new ProgressUpdate(SCAN, checked[0], total[0], found[0], 0,
                    completed ? COMPLETED : FAILED));
        }
        return result;
    }

    private void visitFiles(int depth, FileAction action) throws IOException {
        Files.walkFileTree(basePath, Set.of(), depth, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (attrs.isRegularFile()) {
                    action.accept(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @FunctionalInterface
    private interface FileAction {
        void accept(Path path) throws IOException;
    }

}
