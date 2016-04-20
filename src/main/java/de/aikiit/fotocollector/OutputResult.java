package de.aikiit.fotocollector;

import com.google.common.base.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public Path flush(Path basePath) throws IOException {

        if(!isEmpty()) {
            Files.write(Paths.get(basePath.toString(),name), result.getBytes("UTF-8"));

        }



        // TODO: performs the writing to the filesystem
        // In case the file exists, add .1 etc until it does not exist anymore and can be written from the result
        return null;
    }

}
