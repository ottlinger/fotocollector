package de.aikiit.fotocollector;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.aikiit.fotocollector.main.DateJacksonSerializer;

import java.util.Date;

/**
 * A scan entry defines a specific file that is found.
 *
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public class ScanEntry {

    // TODO use lombok to get rid of the boilerplate
    private final String fileName;

    /**
     * SHA-1 hashValue of contents of an entry.
     */
    private final String hashOverContent = null;

    private final Date creationDate;

    private long size = 0l;

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

    @JsonSerialize(using=DateJacksonSerializer.class)
    public Date getCreationDate() {
        return creationDate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(final long size) {
        this.size = size;
    }

    public String getHashOverContent() {
        return hashOverContent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ScanEntry scanEntry = (ScanEntry) o;

        if (size != scanEntry.size) return false;
        if (fileName != null ? !fileName.equals(scanEntry.fileName) : scanEntry.fileName != null) return false;
        if (hashOverContent != null ? !hashOverContent.equals(scanEntry.hashOverContent) : scanEntry.hashOverContent != null)
            return false;
        return creationDate != null ? creationDate.equals(scanEntry.creationDate) : scanEntry.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (hashOverContent != null ? hashOverContent.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ScanEntry{" +
                "fileName='" + fileName + '\'' +
                ", hashOverContent='" + hashOverContent + '\'' +
                ", creationDate=" + creationDate +
                ", size=" + size +
                '}';
    }
}
