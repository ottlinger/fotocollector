# Fotocollector

Helper to generate metadata about foto collections to not copy image files again and again :-)
This metadata is supposed to be used to recreate collections of files.

## Status

[![Build Status](https://travis-ci.org/ottlinger/fotocollector.svg?branch=master)](https://travis-ci.org/ottlinger/fotocollector)

[![codecov](https://codecov.io/gh/ottlinger/fotocollector/branch/master/graph/badge.svg)](https://codecov.io/gh/ottlinger/fotocollector)

[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/ottlinger/fotocollector.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/ottlinger/fotocollector/context:java)

[![Codacy Badge](https://api.codacy.com/project/badge/grade/1069017d3898425095363374b2519b03)](https://www.codacy.com/app/github_25/fotocollector)
                
[![Known Vulnerabilities](https://snyk.io/test/github/ottlinger/fotocollector/badge.svg)](https://snyk.io/test/github/ottlinger/fotocollector)

https://github.com/users/ottlinger/projects/3

## Howtos

### Build yourself

The tool can easily be built via gradle. It's main goal is clean install, so just run:
```
$ gradle
$ ./gradlew - to use the wrapper
```
after having checked out.

In case you are using the wrapper, the following [wrapper version](./gradle/wrapper/gradle-wrapper.properties) is used.

### Run the tool

Since the tool is still in development there's no command-line at the moment.

#### UberJAR

The application is generated as a shadow-JAR, that may be launched in the following way:
```
$ ./gradlew
$ java -jar build/libs/fotocollector-0.0.2-SNAPSHOT-all.jar
```

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
