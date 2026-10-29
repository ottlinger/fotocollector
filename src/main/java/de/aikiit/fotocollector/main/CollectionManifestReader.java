package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Reads the JSON array or HTML table emitted by FotoCollector. */
public final class CollectionManifestReader {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    public record Entry(String fileName, long size, String hash) { }

    public List<Entry> read(Path input) throws IOException {
        if (!Files.isRegularFile(input)) {
            throw new IOException("Report is not a regular file: " + input);
        }
        // OutputResult appends _0, _1, ... when a previous report already exists.
        String name = input.getFileName().toString().toLowerCase(Locale.ROOT).replaceFirst("_\\d+$", "");
        if (name.endsWith(".json")) {
            return readJson(input);
        }
        if (name.endsWith(".html") || name.endsWith(".htm")) {
            return readHtml(input);
        }
        throw new IOException("Unsupported report format; expected .json, .html or .htm: " + input);
    }

    private List<Entry> readJson(Path input) throws IOException {
        try {
            JsonNode entries = MAPPER.readTree(input.toFile());
            // Also accept the object-shaped representation used by JsonInputReader.
            if (entries != null && entries.isObject()) {
                entries = entries.get("entries");
            }
            if (entries == null || !entries.isArray()) {
                throw new IOException("JSON report must contain an array of entries");
            }
            List<Entry> result = new ArrayList<>();
            for (JsonNode node : entries) {
                JsonNode name = node.get("fileName");
                JsonNode size = node.get("size");
                JsonNode hash = node.get("hashOverContent");
                if (name == null || !name.isString() || size == null
                        || !size.isIntegralNumber() || !size.canConvertToLong()
                        || (hash != null && !hash.isNull() && !hash.isString())) {
                    throw new IOException("Invalid JSON report entry: " + node);
                }
                result.add(entry(name.asString(), size.asLong(),
                        hash == null || hash.isNull() ? null : hash.asString()));
            }
            return List.copyOf(result);
        } catch (JacksonException e) {
            throw new IOException("Cannot parse JSON report: " + input, e);
        }
    }

    private List<Entry> readHtml(Path input) throws IOException {
        Element table = Jsoup.parse(input.toFile(), StandardCharsets.UTF_8.name())
                .selectFirst("table");
        if (table == null) {
            throw new IOException("HTML report contains no table");
        }
        Elements rows = table.select("tr");
        if (rows.isEmpty()) {
            throw new IOException("HTML report contains no table header");
        }
        Elements headers = rows.first().select("th");
        if (headers.size() != 4 || !headers.get(1).text().equals("Filename")
                || !headers.get(2).text().equals("Size/Bytes")
                || !headers.get(3).text().equals("Hash (" + FileUtil.HASH_ALGORITHM + ")")) {
            throw new IOException("Unsupported HTML report table or hash algorithm");
        }
        List<Entry> result = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            Elements cells = rows.get(i).select("td");
            if (cells.size() != 4) {
                throw new IOException("Invalid HTML report row: " + (i + 1));
            }
            try {
                result.add(entry(cells.get(1).wholeText(), Long.parseLong(cells.get(2).text()),
                        cells.get(3).text()));
            } catch (NumberFormatException e) {
                throw new IOException("Invalid file size in HTML report row: " + (i + 1), e);
            }
        }
        return List.copyOf(result);
    }

    private Entry entry(String name, long size, String hash) throws IOException {
        // Reports name individual files, not paths. Never allow a report to escape the output directory.
        if (name.isBlank() || name.equals(".") || name.equals("..")
                || name.contains("/") || name.contains("\\") || name.indexOf('\0') >= 0
                || name.contains(":")) {
            throw new IOException("Invalid filename in report: " + name);
        }
        if (size < 0) {
            throw new IOException("Negative file size in report: " + name);
        }
        if (hash == null || hash.equalsIgnoreCase("none") || hash.equalsIgnoreCase("null")) {
            hash = null;
        } else if (!hash.matches("[a-fA-F0-9]{64}")) {
            throw new IOException("Invalid SHA3-256 hash in report: " + name);
        } else {
            hash = hash.toLowerCase(Locale.ROOT);
        }
        return new Entry(name, size, hash);
    }
}
