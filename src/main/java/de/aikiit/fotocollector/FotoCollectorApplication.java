package de.aikiit.fotocollector;

import de.aikiit.fotocollector.main.FileCollector;
import de.aikiit.fotocollector.main.HtmlOutputWriter;
import de.aikiit.fotocollector.main.JsonOutputWriter;
import de.aikiit.fotocollector.main.PictureScanner;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Play around with Tika to extract and find images.
 *
 * @author hirsch
 * @version 2016-02-19, 22:03
 */
@Slf4j
public class FotoCollectorApplication {

    private static final String USAGE = """
            Usage:
              fotocollector [folder]
              fotocollector collect --input <report.json|report.html> --start <folder> --output <folder>

            collect searches recursively and copies matching files without overwriting existing files.
            """;

    public static void main(final String[] args) throws IOException {
        if (args != null && args.length > 0) {
            if (args[0].equals("--help") || (args[0].equals("collect")
                    && args.length == 2 && args[1].equals("--help"))) {
                System.out.print(USAGE);
                return;
            }
            if (args[0].equals("collect")) {
                collect(args);
                return;
            }
        }
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

    private static void collect(String[] args) throws IOException {
        Map<String, Path> options = new HashMap<>();
        Set<String> allowed = Set.of("--input", "--start", "--output");
        for (int i = 1; i < args.length; i += 2) {
            if (!allowed.contains(args[i]) || i + 1 >= args.length
                    || args[i + 1].startsWith("--") || args[i + 1].isBlank()
                    || options.containsKey(args[i])) {
                throw new IllegalArgumentException("Invalid or duplicate collect option: " + args[i] + "\n" + USAGE);
            }
            options.put(args[i], Paths.get(args[i + 1]));
        }
        if (!options.keySet().equals(allowed)) {
            throw new IllegalArgumentException("collect requires --input, --start and --output\n" + USAGE);
        }
        FileCollector.Result result = new FileCollector().collect(options.get("--input"),
                options.get("--start"), options.get("--output"));
        log.info("Collection complete: {} copied, {} missing", result.copiedFiles().size(), result.missingFiles().size());
        if (!result.missingFiles().isEmpty()) {
            throw new IOException("Files not found: " + String.join(", ", result.missingFiles()));
        }
    }
}
