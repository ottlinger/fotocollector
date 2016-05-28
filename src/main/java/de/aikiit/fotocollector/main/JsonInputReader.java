package de.aikiit.fotocollector.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.aikiit.fotocollector.InputReader;
import de.aikiit.fotocollector.ScanResult;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author hirsch
 * @version 2016-05-25, 23:21
 */
public class JsonInputReader implements InputReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public ScanResult read(Path input) {
        try {
            return MAPPER.readValue(input.toFile(), ScanResult.class);
        } catch (NullPointerException | IOException e) {
            return new ScanResult();
        }
    }

}
