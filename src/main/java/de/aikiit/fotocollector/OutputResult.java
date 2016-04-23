package de.aikiit.fotocollector;

import com.google.common.base.Strings;
import de.aikiit.fotocollector.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Container of a scan result being ready to
 *
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
public class OutputResult {

    private final String result;
    private final String name;

    public OutputResult(String result, String name) {
        this.result = result;
        this.name = name;
    }

    public String getResult() {
        return this.result;
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(result);
    }

    public Optional<Path> flush(Path basePath) throws IOException {

        if(!isEmpty()) {
            final Path targetPath = FileUtil.makeUnique(basePath, name);
            Files.write(targetPath, result.getBytes("UTF-8"));
            return Optional.of(targetPath);
        }
        return Optional.empty();
    }



}
