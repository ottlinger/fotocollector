package de.aikiit.fotocollector.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tools.jackson.core.JsonGenerator;

import java.time.Instant;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DateJacksonSerializerTest {

    private final DateJacksonSerializer serializer = new DateJacksonSerializer();
    private final JsonGenerator generator = mock(JsonGenerator.class);

    @Test
    void serializesNullAsNone() {
        serializer.serialize(null, generator, null);

        verify(generator).writeString("none");
        verifyNoMoreInteractions(generator);
    }

    @ParameterizedTest
    @CsvSource({
            "1970-01-01T00:00:00Z, 1970-01-01 00:00:00",
            "1969-12-31T23:59:59Z, 1969-12-31 23:59:59",
            "2024-02-29T01:02:03Z, 2024-02-29 01:02:03",
            "2024-07-15T23:45:56Z, 2024-07-15 23:45:56",
            "2024-12-31T23:59:59.999Z, 2024-12-31 23:59:59"
    })
    void serializesDatesInGmtWithoutMilliseconds(String instant, String expected) {
        serializer.serialize(Date.from(Instant.parse(instant)), generator, null);

        verify(generator).writeString(expected);
        verifyNoMoreInteractions(generator);
    }
}
