package de.aikiit.fotocollector.main;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;
import de.aikiit.fotocollector.OutputResult;
import de.aikiit.fotocollector.OutputWriter;
import de.aikiit.fotocollector.ScanResult;

/**
 * @author hirsch
 * @version 2016-02-20, 14:09
 */
public class JsonOutputWriter implements OutputWriter {

    // Preserve the existing JSON property order rather than Jackson 3's alphabetical default.
    private static final JsonMapper MAPPER = JsonMapper.builder()
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .build();
    private static final String NAME = "fotocollector.json";

    @Override
    public OutputResult write(final ScanResult result) {

        if (result == null || result.isEmpty()) {
            return new OutputResult(null, NAME);
        }

        try {
            return new OutputResult(MAPPER.writeValueAsString(result.getEntries()), NAME);
        } catch (JacksonException e) {
            return new OutputResult(null, NAME);
        }

    }
}
