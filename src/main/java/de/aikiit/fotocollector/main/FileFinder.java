package de.aikiit.fotocollector.main;

import de.aikiit.fotocollector.ScanResult;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.Files.walk;

/**
 * Finds/traverses from a given base path in order to find files.
 *
 * @author hirsch
 * @version 2016-05-20, 22:38
 */
public class FileFinder {

    public static void main(String... args) throws IOException{

        // https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#find-java.nio.file.Path-int-java.util.function.BiPredicate-java.nio.file.FileVisitOption...-
        final URI path = Paths.get("/tmp/").toUri();

        walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .forEach(System.out::println);
    }

    public static void scan(ScanResult result) {


    }




}
