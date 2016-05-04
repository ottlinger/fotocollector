package de.aikiit.fotocollector.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.OutputWriter;
import de.aikiit.fotocollector.ScanResult;

/**
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public class JsonOutputWriter implements OutputWriter {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String NAME = "fotocollector.json";

    @Override
    public OutputResult write(final ScanResult result) {

        if (result == null || result.isEmpty()) {
            return new OutputResult(null, NAME);
        }

        try {
            return new OutputResult(MAPPER.writeValueAsString(result.getEntries()), NAME);
        } catch (JsonProcessingException e) {
            return new OutputResult(null, NAME);
        }

    }
}
