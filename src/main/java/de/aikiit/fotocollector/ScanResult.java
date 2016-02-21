package de.aikiit.fotocollector;


import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
public class ScanResult {

    private Set<ScanEntry> entries= Sets.newHashSet();

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
