package de.aikiit.fotocollector;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.aikiit.fotocollector.main.DateJacksonSerializer;
import lombok.Data;

import java.util.Date;

/**
 * A scan entry defines a specific file that is found.
 *
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
@Data
public class ScanEntry {

    private final String fileName;

    /**
     * SHA-1 hashValue of contents of an entry.
     */
    private String hashOverContent = null;

    private final Date creationDate;

    private long size = 0L;

    public ScanEntry(String fileName) {
        this(fileName, new Date());
    }

    public ScanEntry(String fileName, Date creationDate) {
        this.fileName = fileName;
        this.creationDate = creationDate;
    }

    @JsonSerialize(using=DateJacksonSerializer.class)
    public Date getCreationDate() {
        return creationDate;
    }

}
