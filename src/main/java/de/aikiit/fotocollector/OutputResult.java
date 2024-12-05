package de.aikiit.fotocollector;

import com.google.common.base.Strings;
import de.aikiit.fotocollector.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Container of a scan result being ready to be processed.
 *
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
@Slf4j
public record OutputResult(String result, String name) {

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(result);
    }

    public Optional<Path> flush(Path basePath) throws IOException {
        if (!isEmpty()) {
            final Path targetPath = FileUtil.makeUnique(basePath, name);
            Files.write(targetPath, result.getBytes(StandardCharsets.UTF_8));
            log.info("Result was written in {}", targetPath.toRealPath(LinkOption.NOFOLLOW_LINKS));

            return Optional.of(targetPath);
        } else {
            log.info("Empty result is not written into {}", name);
        }
        return Optional.empty();
    }
}
