package de.aikiit.fotocollector;

import com.google.common.base.Strings;

import java.nio.file.Path;

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

    public Path flush(Path basePath) {
        // TODO: performs the writing to the filesystem
        // In case the file exists, add .1 etc until it does not exist anymore and can be written from the result
        return null;
    }

}
