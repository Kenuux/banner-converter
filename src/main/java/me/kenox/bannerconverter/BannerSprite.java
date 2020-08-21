package me.kenox.bannerconverter;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Kenox
 */
public class BannerSprite {

    @Getter private final String resource;
    private final BufferedImage bufferedImage;

    private final int tileCountX;
    private final int tileCountY;

    public BannerSprite(final String resource, final int tileCountX, final int tileCountY) throws IOException {
        this.resource = resource;
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        this.bufferedImage = ImageIO.read(Objects.requireNonNull(classloader.getResourceAsStream(resource)));

        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
    }

    public BufferedImage getTile(final int x, final int y) {
        return this.bufferedImage.getSubimage(x, y, this.bufferedImage.getWidth() / this.tileCountX, this.bufferedImage.getHeight() / this.tileCountY);
    }
}
