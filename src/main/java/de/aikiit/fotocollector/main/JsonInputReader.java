package de.aikiit.fotocollector.main;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;
import de.aikiit.fotocollector.InputReader;
import de.aikiit.fotocollector.ScanResult;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * @author hirsch
 * @version 2016-05-25, 23:21
 */
public class JsonInputReader implements InputReader {

    private static final JsonMapper MAPPER = JsonMapper.builder()
            // ScanResult exposes its entries through a getter, without a setter.
            .enable(MapperFeature.USE_GETTERS_AS_SETTERS)
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    @Override
    public ScanResult read(Path input) {
        try {
            return MAPPER.readValue(input.toFile(), ScanResult.class);
        } catch (NullPointerException | JacksonException | InvalidPathException e) {
            return new ScanResult();
        }
    }

}
