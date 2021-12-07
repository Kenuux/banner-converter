package de.kenox.bannerconverter;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Kenox
 */
public class BannerSprite {

    @Getter
    private final String resource;
    private final BufferedImage bufferedImage;

    private final int tileCountX;
    private final int tileCountY;

    public BannerSprite(final String resource, final int tileCountX, final int tileCountY) throws IOException {
        this.resource = resource;
        this.bufferedImage = ImageIO.read(this.getFileFromResourceAsStream(resource));

        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public BufferedImage getTile(final int x, final int y) {
        return this.bufferedImage.getSubimage(x, y, this.bufferedImage.getWidth() / this.tileCountX, this.bufferedImage.getHeight() / this.tileCountY);
    }
}
