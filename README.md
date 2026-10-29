<!-- markdownlint-disable-file MD013 -->
# FotoCollector

Helper to generate metadata about photo collections to not copy image files again and again :-)
This metadata is supposed to be used to recreate collections of files.

## Abstract

Usually you do have a lot of photos you want to keep. Sometimes you copy subsets of these in order to print them or to send them somewhere.
FotoCollector helps you with that:

* Just copy all of your desired pictures into one folder.
* Let FotoCollector run over that folder in order to create a JSON (machine-readable) and HTML (human-readable) report.
* Recreate the collection with the `collect` command: scan your image base directory and copy the desired files from a JSON or HTML report into a separate output folder.
* Thus you can recreate collections of photographs based on metadata.

## Status

[![Github Action master branch status](https://github.com/ottlinger/fotocollector/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/ottlinger/fotocollector/actions)

[![codecov](https://codecov.io/gh/ottlinger/fotocollector/branch/master/graph/badge.svg?token=fGTWQYT78p)](https://codecov.io/gh/ottlinger/fotocollector)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/331a0467219c44a8978d79de617ad7e4)](https://www.codacy.com/gh/ottlinger/fotocollector/dashboard)

[![Known Vulnerabilities](https://snyk.io/test/github/ottlinger/fotocollector/badge.svg)](https://snyk.io/test/github/ottlinger/fotocollector)

## Howtos

### Build yourself

The tool can easily be built via [Gradle](https://gradle.org). Its main goal is a clean install, so just run:

```bash
$ gradle
OR
$ ./gradlew - to use the wrapper
```

after having checked out.

In case you are using the wrapper, the following [wrapper version](./gradle/wrapper/gradle-wrapper.properties) is used.

### Run the tool

Requires Java 17 or newer. Pass a folder to generate reports, or use `collect` to recreate a collection from an existing report. Use `--help` for command syntax.

#### UberJAR

The application is generated as a shadow-JAR, that may be launched in the following way:

```bash
$ ./gradlew
BUILD SUCCESSFUL in 1s
14 actionable tasks: 10 executed, 4 from cache
$ java -jar build/libs/fotocollector-1.0.0-SNAPSHOT-all.jar .
```

to run in the current directory and generate the following output formats:

* fotocollector.html - HTML report
* fotocollector.json - JSON report

#### Collect files from a report

```bash
java -jar build/libs/fotocollector-1.0.0-SNAPSHOT-all.jar collect \
  --input /path/to/fotocollector.json \
  --start /path/to/photo-archive \
  --output /path/to/recreated-collection
```

`--input` also accepts the generated HTML report (`.html` or `.htm`). All three options are required; paths containing spaces should be quoted.

* The start folder is searched **recursively**, without following symbolic links. The output folder is created if necessary and excluded from the search if it is inside the start folder. It must not equal or contain the start folder.
* Files are matched by size and the report's **SHA3-256 content hash**, so moved or renamed files can be found. Each report entry is copied once, using its original filename in a flat output directory. Duplicate source files do not produce extra copies.
* If a report entry has no hash (`null` or `none`), matching falls back to **filename and size**. This cannot verify content identity; use reports with hashes when possible.
* Source files are never moved or deleted. Existing output files are never overwritten: collisions get a numeric infix before the extension, such as `photo_0.jpg`.
* The command logs a summary of copied and missing files. Missing files produce an error and a nonzero exit status, while files already collected remain in the output folder. Invalid reports and I/O failures also fail the command.
* JSON input may be the generated array or an object containing an `entries` array. Malformed entries, unsafe filenames, and invalid hashes are rejected before copying.

## License

This tool is licensed under [Apache License v2.0](https://www.apache.org/licenses/).

## Release history

### 0.0.2 - 2026-10-29

[Mainly](https://github.com/ottlinger/fotocollector/tree/v0.0.2) dependency updates and some refactoring. The HTML report is now generated with [Thymeleaf](https://www.thymeleaf.org/).

### 0.0.1 - 2016-03-29

This [first release](https://github.com/ottlinger/fotocollector/tree/0.0.1) contains some Tika experiments and a basic skeleton for JSON and HTML generation.
Furthermore I experienced the ease of releasing with gradle, it nails down to a

```bash
gradle release
```

due to the [Release-Plugin](https://github.com/researchgate/gradle-release).

## Project idea

### 2015-11

Project idea grew when I wanted to generate some Xmas-related image collections.

### 2016-02

The idea is to scan for images and generate a JSON file structure with a collection's title and the list of image names.
These names can later be used to scan a certain directory and copy the image to a before mentioned directory in order to restore a collection without keeping dozens of copies of the original images.

### 2016-03

The output should be available as JSON and HTML.
