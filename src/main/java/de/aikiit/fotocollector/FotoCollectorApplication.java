package de.aikiit.fotocollector;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

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

        // TODO: in case we want to read/write files
        // http://www.javapractices.com/topic/TopicAction.do?Id=42

        // doc - Exception
        // docs - Exception
        // png - Caused by: javax.imageio.IIOException: I/O error reading PNG header!
        // odt - Mime: application/vnd.oasis.opendocument.text - success :-)
        File file = new File("/tmp/a.odt");

/*
        try (InputStream stream = Files.newInputStream(file.toPath())) {
        Metadata metadata = new Metadata();
            parser.parse(stream, handler, metadata);
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
            parser.parse(stream, handler, metadata);
            System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
            System.out.println("Title: " + metadata.get(TikaCoreProperties.TITLE));
            System.out.println("Author: " + metadata.get(Office.AUTHOR));
//            System.out.println("content: " + handler.toString());

            System.out.println(handler.toString());
        }
*/

        // exception: thread "main" java.io.IOException: mark/reset not supported
        // https://tika.apache.org/1.12/detection.html
        TikaConfig tika = new TikaConfig();
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.toString());
            String mimetype = tika.getDetector().detect(
                    stream, metadata).getType();
            System.out.println("File " + file + " is " + mimetype);
        }

    }
}
