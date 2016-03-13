package de.aikiit.fotocollector.main;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author hirsch
 * @version 2016-03-10, 23:38
 */
public class DateJacksonSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(final Date value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException {

        if (value == null) {
            gen.writeString("none");
            return;
        }

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        gen.writeString(sdf.format(value));
    }
}
