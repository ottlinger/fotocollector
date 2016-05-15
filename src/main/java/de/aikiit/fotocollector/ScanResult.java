package de.aikiit.fotocollector;


import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.Set;

/**
 * A scan result defines which entries were found.
 * A result is never null, but maybe empty instead.
 *
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
public class ScanResult {

    private static Comparator<ScanEntry> BY_NAME = (e1, e2) -> (e1.getFileName().compareTo(e2.getFileName()));

    private final Set<ScanEntry> entries = Sets.newTreeSet(BY_NAME);

    public Set<ScanEntry> getEntries() {
        return Sets.newHashSet(entries);
    }

    public void addEntry(ScanEntry entry) {
        entries.add(entry);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ScanResult that = (ScanResult) o;

        return entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    @Override
    public String toString() {
        return "ScanResult{" +
                "entries=" + entries +
                '}';
    }
}
