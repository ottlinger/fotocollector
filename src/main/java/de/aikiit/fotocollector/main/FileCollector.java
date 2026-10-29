package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.main.CollectionManifestReader.Entry;
import de.aikiit.fotocollector.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Recreates a collection from a report without modifying the source files. */
@Slf4j
public final class FileCollector {

    public record Result(List<Path> copiedFiles, List<String> missingFiles) {
        public Result {
            copiedFiles = List.copyOf(copiedFiles);
            missingFiles = List.copyOf(missingFiles);
        }
    }

    public Result collect(Path input, Path start, Path output) throws IOException {
        List<Entry> entries = new CollectionManifestReader().read(input);
        Path source = start.toRealPath();
        if (!Files.isDirectory(source)) {
            throw new IOException("Start folder is not a directory: " + start);
        }
        Files.createDirectories(output);
        Path destination = output.toRealPath();
        if (source.startsWith(destination)) {
            throw new IOException("Output directory must not equal or contain the start folder");
        }

        // Size-filter first, and hash each candidate at most once, regardless of report size.
        Map<Long, List<Entry>> remaining = new LinkedHashMap<>();
        for (Entry entry : entries) {
            remaining.computeIfAbsent(entry.size(), ignored -> new ArrayList<>()).add(entry);
        }
        List<Path> copied = new ArrayList<>();
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (remaining.isEmpty()) {
                    return FileVisitResult.TERMINATE;
                }
                return dir.equals(destination) ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // walkFileTree does not follow symbolic links; only copy real, regular files.
                List<Entry> candidates = remaining.get(attrs.size());
                if (!attrs.isRegularFile() || candidates == null) {
                    return FileVisitResult.CONTINUE;
                }
                String hash = candidates.stream().anyMatch(entry -> entry.hash() != null)
                        ? FileUtil.getHash(file) : null;
                var iterator = candidates.iterator();
                while (iterator.hasNext()) {
                    Entry entry = iterator.next();
                    boolean matches = entry.hash() != null ? entry.hash().equals(hash)
                            : entry.fileName().equals(file.getFileName().toString());
                    if (matches) {
                        Path target = FileUtil.makeUnique(destination, entry.fileName());
                        // Deliberately omit REPLACE_EXISTING: a collision must never destroy a file.
                        Files.copy(file, target);
                        copied.add(target);
                        iterator.remove();
                        log.info("Collected {} -> {}", file, target);
                    }
                }
                if (candidates.isEmpty()) {
                    remaining.remove(attrs.size());
                }
                return remaining.isEmpty() ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException error) {
                log.warn("Cannot scan {}: {}", file, error.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });
        List<String> missing = remaining.values().stream().flatMap(List::stream)
                .map(Entry::fileName).toList();
        return new Result(copied, missing);
    }
}
