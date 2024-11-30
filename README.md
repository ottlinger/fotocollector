# FotoCollector

Helper to generate metadata about photo collections to not copy image files again and again :-)
This metadata is supposed to be used to recreate collections of files.

## Status

[![Github Action master branch status](https://github.com/ottlinger/fotocollector/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/ottlinger/fotocollector/actions)

[![codecov](https://codecov.io/gh/ottlinger/fotocollector/branch/master/graph/badge.svg?token=fGTWQYT78p)](https://codecov.io/gh/ottlinger/fotocollector)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/331a0467219c44a8978d79de617ad7e4)](https://www.codacy.com/gh/ottlinger/fotocollector/dashboard)

[![Known Vulnerabilities](https://snyk.io/test/github/ottlinger/fotocollector/badge.svg)](https://snyk.io/test/github/ottlinger/fotocollector)

## Howtos

### Build yourself

The tool can easily be built via gradle. Its main goal is a clean install, so just run:

```bash
$ gradle
$ ./gradlew - to use the wrapper
```

after having checked out.

In case you are using the wrapper, the following [wrapper version](./gradle/wrapper/gradle-wrapper.properties) is used.

### Run the tool

Since the tool is still in development there's no command-line at the moment.

#### UberJAR

The application is generated as a shadow-JAR, that may be launched in the following way:

```bash
$ ./gradlew
$ java -jar build/libs/fotocollector-0.0.2-SNAPSHOT-all.jar .
```

to run in the current directory and generate the following output formats:

* fotocollector.html - HTMl report
* fotocollector.json - JSON report

## Releases

### 0.0.2-SNAPSHOT

Currently: work in progress :-D

### 0.0.1 - 20160329

This [first release](https://github.com/ottlinger/fotocollector/tree/0.0.1) contains some Tika experiments and a basic skeleton for JSON and HTML generation.
Furthermore I experienced the ease of releasing with gradle, it nails down to a

```
$ gradle release
```

## History

### 2015-11

Project idea grew when I wanted to generate some Xmas-related image collections.

### 2016-02

The idea is to scan for images and generate a JSON file structure with a collection's title and the list of image names.
These names can later be used to scan a certain directory and copy the image to a before mentioned directory in order to restore a collection without keeping dozens of copies of the original images.

### 2016-03

The output should be available as JSON and HTML.

## License

This tool is licensed under [Apache License v2.0](https://www.apache.org/licenses/).
