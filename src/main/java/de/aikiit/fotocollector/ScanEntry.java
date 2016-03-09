package de.aikiit.fotocollector;

import java.util.Date;

/**
 * A scan entry defines a specific file that is found.
 *
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public class ScanEntry {

    // TODO is it useful to add dates from EXIF?!
    private final String fileName;
    private final Date creationDate;

    public ScanEntry(String fileName) {
        this(fileName, new Date());
    }

    public ScanEntry(String fileName, Date creationDate) {
        this.fileName = fileName;
        this.creationDate = creationDate;
    }

    public String getFileName() {
        return fileName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ScanEntry scanEntry = (ScanEntry) o;

        return fileName.equals(scanEntry.fileName) && creationDate.equals(scanEntry.creationDate);

    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + creationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ScanEntry{" +
                "fileName='" + fileName + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
