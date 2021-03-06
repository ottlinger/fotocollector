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

        System.out.println("___________     __         _________        .__  .__                 __                \n" +
                "\\_   _____/____/  |_  ____ \\_   ___ \\  ____ |  | |  |   ____   _____/  |_  ___________ \n" +
                " |    __)/  _ \\   __\\/  _ \\/    \\  \\/ /  _ \\|  | |  | _/ __ \\_/ ___\\   __\\/  _ \\_  __ \\\n" +
                " |     \\(  <_> )  | (  <_> )     \\___(  <_> )  |_|  |_\\  ___/\\  \\___|  | (  <_> )  | \\/\n" +
                " \\___  / \\____/|__|  \\____/ \\______  /\\____/|____/____/\\___  >\\___  >__|  \\____/|__|   \n" +
                "     \\/                            \\/                      \\/     \\/                   ");

        System.out.println("Starting collection on directory: " + files.toRealPath(LinkOption.NOFOLLOW_LINKS));

        final ScanResult scanResult = new PictureScanner(files).getFiles();

        if (scanResult.isEmpty()) {
            System.out.println("No results found. Nothing to be done :-)");
        } else {
            final OutputResult html = new HtmlOutputWriter().write(scanResult);
            final OutputResult json = new JsonOutputWriter().write(scanResult);

            html.flush(files);
            json.flush(files);
        }
    }
}
