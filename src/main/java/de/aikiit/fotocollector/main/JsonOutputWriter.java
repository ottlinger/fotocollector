package de.aikiit.fotocollector.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.OutputWriter;
import de.aikiit.fotocollector.ScanEntry;
import de.aikiit.fotocollector.ScanResult;

/**
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public class JsonOutputWriter implements OutputWriter {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public OutputResult write(final ScanResult result) {

        if(result == null || result.isEmpty()) {
            return new OutputResult(null);
        }

        StringBuilder res = new StringBuilder();
        for(ScanEntry entry : result.getEntries()) {
            // TODO jsonify
            res.append(entry.getFileName()).append(",");
        }

        return new OutputResult(res.toString());

    }
}
