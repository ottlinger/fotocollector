package de.aikiit.fotocollector;

import org.junit.Test;

import java.io.IOException;

/**
 * @author hirsch
 * @version 2016-04-28, 23:13
 */
public class FotoCollectorApplicationTest {
    @Test
    public void launchWithoutArgs() throws IOException {
        FotoCollectorApplication.main(null);
    }
}
