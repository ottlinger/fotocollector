package de.aikiit.fotocollector;

import tools.jackson.databind.annotation.JsonSerialize;
import de.aikiit.fotocollector.main.DateJacksonSerializer;

import java.util.Date;

/**
 * A scan entry defines a specific file that is found.
 *
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public record ScanEntry(
        String fileName,
        // SHA-1 hash of the entry's contents.
        String hashOverContent,
        @JsonSerialize(using = DateJacksonSerializer.class) Date creationDate,
        long size) {

    public ScanEntry {
        creationDate = creationDate == null ? null : new Date(creationDate.getTime());
    }

    public ScanEntry(String fileName) {
        this(fileName, new Date());
    }

    public ScanEntry(String fileName, Date creationDate) {
        this(fileName, null, creationDate, 0L);
    }

    @Override
    public Date creationDate() {
        return creationDate == null ? null : new Date(creationDate.getTime());
    }

}
