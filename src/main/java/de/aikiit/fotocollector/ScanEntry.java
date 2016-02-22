package de.aikiit.fotocollector;

/**
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public class ScanEntry {

    private final String fileName;

    public ScanEntry(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
