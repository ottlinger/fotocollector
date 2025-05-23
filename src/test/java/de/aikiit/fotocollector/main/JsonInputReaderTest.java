package de.aikiit.fotocollector.main;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author hirsch
 * @version 2016-05-25, 23:25
 */
public class JsonInputReaderTest {

    @Test
    public void nullParameterYieldsEmptyResult() {
        assertThat(new JsonInputReader().read(null).isEmpty()).isTrue();
    }

    @Test
    public void illegalPathYieldsEmptyResultAndNoException() {
        assertThat(new JsonInputReader().read(new File("FILEDOESnotExRight" + System.currentTimeMillis()).toPath()).isEmpty()).isTrue();
    }

}
