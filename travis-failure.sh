#!/bin/bash
echo "Since we cannot reach the tests you may cat a problematic test here - run 'gradle test' locally to see its name."
echo 
echo "***** ALL TESTS *****"
ls build/test-results/*.xml
echo
echo "***** A SPECIFIC ONE *****"
cat build/test-results/TEST-de.aikiit.fotocollector.main.JsonOutputWriterTest.xml
