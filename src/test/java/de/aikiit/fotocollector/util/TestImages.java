package de.aikiit.fotocollector.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/** Generates real, decodable image data independently of the destination's extension. */
public final class TestImages {

    private TestImages() {
    }

    public static Path write(Path path) throws IOException {
        return write(path, "png");
    }

    public static Path write(Path path, String format) throws IOException {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x123456);
        if (!ImageIO.write(image, format, path.toFile())) {
            throw new IOException("No ImageIO writer available for " + format);
        }
        return path;
    }
}
