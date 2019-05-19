package de.aikiit.fotocollector;


import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Comparator;
import java.util.Set;

/**
 * A scan result defines which entries were found.
 * A result is never null, but maybe empty instead.
 *
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
@Data
public class ScanResult {

    private static Comparator<ScanEntry> BY_NAME = (e1, e2) -> (e1.getFileName().compareTo(e2.getFileName()));

    private final Set<ScanEntry> entries = Sets.newTreeSet(BY_NAME);

    public void addEntry(ScanEntry entry) {
        entries.add(entry);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }
}
