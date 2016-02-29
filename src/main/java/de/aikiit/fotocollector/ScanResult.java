package de.aikiit.fotocollector;


import com.google.common.collect.Sets;

import java.util.Set;

/**
 * A scan result defines which entries were found.
 * A result is never null, but maybe empty instead.
 *
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
public class ScanResult {

    private final Set<ScanEntry> entries = Sets.newHashSet();

    public Set<ScanEntry> getEntries() {
        return Sets.newHashSet(entries);
    }

    public void addEntry(ScanEntry entry) {
        entries.add(entry);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

}
