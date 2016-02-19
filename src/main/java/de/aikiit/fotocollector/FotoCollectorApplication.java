package de.aikiit.fotocollector;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Play around with Tika to extract and find images.
 *
 * @author hirsch
 * @version 2016-02-19, 22:03
 */
public class FotoCollectorApplication {

    public static void main(final String[] args) throws IOException,
            SAXException, TikaException {

        // TODO https://tika.apache.org/1.7/examples.html#Parsing_using_the_Tika_Facade
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        try (InputStream stream = FotoCollectorApplication.class.getResourceAsStream("test.doc")) {
            parser.parse(stream, handler, metadata);
            System.out.println(handler.toString());
        }
    }
}
