package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.OutputWriter;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;

import java.time.LocalDateTime;

/**
 * @author hirsch
 * @version 2016-03-16, 22:24
 */
public class HtmlOutputWriter implements OutputWriter {

    private static final String NAME = "fotocollector.html";

    private static final String HEADER = "<html><head></head><body>";
    private static final String TABLE_HEADER = "<table><tr><th>Filename</th><th>Size</th><th>SHA-1</th></tr>";
    private static final String FOOTER = "</table><hr><p>Created at: %s with %s images</p></body></html>";
    private static final String TABLE_ENTRY = "<tr><td>%s</td><td>%s</td><td>%s</td></tr>";

    @Override
    public OutputResult write(final ScanResult result) {
        if (result == null || result.isEmpty()) {
            return new OutputResult(null, NAME);
        }

        StringBuilder table = new StringBuilder();
        table.append(HEADER);
        table.append(TABLE_HEADER);

        for (ScanEntry entry : result.getEntries()) {
            table.append(String.format(TABLE_ENTRY, entry.getFileName(), entry.getSize(), entry.getHashOverContent()));
        }

        table.append(String.format(FOOTER, LocalDateTime.now(), result.getEntries().size()));
        return new OutputResult(table.toString(), NAME);
    }
}
