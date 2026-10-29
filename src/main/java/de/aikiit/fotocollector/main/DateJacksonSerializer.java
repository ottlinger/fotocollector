package de.aikiit.fotocollector.main;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.SerializationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author hirsch
 * @version 2016-03-10, 23:38
 */
public class DateJacksonSerializer extends ValueSerializer<Date> {
    @Override
    public void serialize(final Date value, final JsonGenerator gen, final SerializationContext context) {

        if (value == null) {
            gen.writeString("none");
            return;
        }

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        gen.writeString(sdf.format(value));
    }
}
