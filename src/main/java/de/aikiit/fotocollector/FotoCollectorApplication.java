package de.aikiit.fotocollector;

import de.aikiit.fotocollector.main.HtmlOutputWriter;
import de.aikiit.fotocollector.main.JsonOutputWriter;
import de.aikiit.fotocollector.main.PictureScanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Play around with Tika to extract and find images.
 *
 * @author hirsch
 * @version 2016-02-19, 22:03
 */
public class FotoCollectorApplication {

    public static void main(final String[] args) throws IOException {
        Path files = ((args != null) && (args.length >= 1)) ? Paths.get(args[0]) : new File(".").toPath();

        System.out.println("Will work on directory: " + files.toRealPath(LinkOption.NOFOLLOW_LINKS));

        final ScanResult scanResult = new PictureScanner(files).getFiles();

        final OutputResult html = new HtmlOutputWriter().write(scanResult);
        final OutputResult json = new JsonOutputWriter().write(scanResult);

        html.flush(files);
        json.flush(files);
    }
}
