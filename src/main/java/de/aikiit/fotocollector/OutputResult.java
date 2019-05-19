package de.aikiit.fotocollector;

import com.google.common.base.Strings;
import de.aikiit.fotocollector.util.FileUtil;
import lombok.Data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Container of a scan result being ready to
 *
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
@Data
public class OutputResult {

    private final String result;
    private final String name;

    public OutputResult(String result, String name) {
        this.result = result;
        this.name = name;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(result);
    }

    public Optional<Path> flush(Path basePath) throws IOException {
        if(!isEmpty()) {
            final Path targetPath = FileUtil.makeUnique(basePath, name);
            Files.write(targetPath, result.getBytes(StandardCharsets.UTF_8));
            return Optional.of(targetPath);
        } else {
            System.out.println("Empty result is not written into " + name);
        }
        return Optional.empty();
    }
}
