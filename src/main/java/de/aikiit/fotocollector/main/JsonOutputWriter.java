package de.aikiit.fotocollector.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.OutputWriter;
import de.aikiit.fotocollector.ScanResult;

/**
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public class JsonOutputWriter implements OutputWriter {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public OutputResult write(final ScanResult result) {
        return null;
    }
}
