package de.aikiit.fotocollector;

/**
 * A scan entry defines a specific file that is found.
 *
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public class ScanEntry {

    // TODO is it useful to add dates from EXIF?!
    private final String fileName;

    public ScanEntry(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
