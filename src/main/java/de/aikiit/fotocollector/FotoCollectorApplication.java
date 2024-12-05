package de.aikiit.fotocollector;

import de.aikiit.fotocollector.main.HtmlOutputWriter;
import de.aikiit.fotocollector.main.JsonOutputWriter;
import de.aikiit.fotocollector.main.PictureScanner;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class FotoCollectorApplication {

    public static void main(final String[] args) throws IOException {
        Path files = ((args != null) && (args.length >= 1)) ? Paths.get(args[0]) : new File(".").toPath();

        log.info("""
                ___________     __         _________        .__  .__                 __               \s
                \\_   _____/____/  |_  ____ \\_   ___ \\  ____ |  | |  |   ____   _____/  |_  ___________\s
                 |    __)/  _ \\   __\\/  _ \\/    \\  \\/ /  _ \\|  | |  | _/ __ \\_/ ___\\   __\\/  _ \\_  __ \\
                 |     \\(  <_> )  | (  <_> )     \\___(  <_> )  |_|  |_\\  ___/\\  \\___|  | (  <_> )  | \\/
                 \\___  / \\____/|__|  \\____/ \\______  /\\____/|____/____/\\___  >\\___  >__|  \\____/|__|  \s
                     \\/                            \\/                      \\/     \\/                  \s""");

        log.info("Starting collection on directory: {}", files.toRealPath(LinkOption.NOFOLLOW_LINKS));

        final ScanResult scanResult = new PictureScanner(files).getFiles();
        if (scanResult.isEmpty()) {
            log.info("No results found. Nothing to be done and no output was written :-)");
        } else {
            final OutputResult html = new HtmlOutputWriter().write(scanResult);
            final OutputResult json = new JsonOutputWriter().write(scanResult);

            html.flush(files);
            json.flush(files);
            log.info("Finished writing all results in {}", files.toRealPath(LinkOption.NOFOLLOW_LINKS));
        }
    }
}
