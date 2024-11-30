package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.OutputWriter;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hirsch
 * @version 2016-03-16, 22:24
 */
public class HtmlOutputWriter implements OutputWriter {

    private static final String NAME = "fotocollector.html";

    private static final String TABLE_ENTRY = "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";
    private static final String HEADER = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>FotoCollector - %s</title> <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /></head><body>";
    private static final String TABLE_HEADER = "<table><tr><th>Number</th><th>Filename</th><th>Size/Bytes</th><th>Hash (SHA-1)</th></tr>";
    private static final String FOOTER = "</table><hr><p>Created at: %s with %s images</p></body></html>";

    @Override
    public OutputResult write(final ScanResult result) {
        if (result == null || result.isEmpty()) {
            return new OutputResult(null, NAME);
        }

        LocalDateTime time = LocalDateTime.now();

        StringBuilder table = new StringBuilder();
        table.append(String.format(HEADER, time));
        table.append(TABLE_HEADER);

        AtomicInteger count = new AtomicInteger(1);

        for (ScanEntry entry : result.getEntries()) {
            table.append(String.format(TABLE_ENTRY, "#" + count.getAndIncrement(), entry.getFileName(), entry.getSize(), entry.getHashOverContent()));
        }

        table.append(String.format(FOOTER, time, result.getEntries().size()));
        return new OutputResult(table.toString(), NAME);
    }
}
