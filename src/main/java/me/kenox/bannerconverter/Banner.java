package me.kenox.bannerconverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kenox
 */
@Data
public class Banner {

    public static final int TILE_COUNT_WIDTH = 5;
    public static final int TILE_COUNT_HEIGHT = 8;

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Banner.class, new BannerInstanceCreator())
            .registerTypeAdapter(BannerColor.class, new BannerColorDeserializer())
            .registerTypeAdapter(BannerTileMetadata.class, new BannerTileMetadataDeserializer())
            .create();

    private final Map<String, BannerSprite> spriteRegistry = new HashMap<>();

    @SerializedName("Base")
    private final BannerColor bannerColor;

    @SerializedName("Patterns")
    private final List<BannerPattern> bannerPatterns;

    public BufferedImage toImage(final int width, final int height) throws IOException {
        BufferedImage base = this.getBannerSprite(this.bannerColor.getResource()).getTile(BannerTileMetadata.BASE.getTileX(), BannerTileMetadata.BASE.getTileY());

        // Iterate through all patterns and overlap base image
        for (final BannerPattern bannerPattern : this.bannerPatterns) {
            final BannerTileMetadata bannerTileMetadata = bannerPattern.getBannerTileMetadata();
            final BannerColor bannerColor = bannerPattern.getBannerColor();
            final BannerSprite bannerSprite = this.getBannerSprite(bannerColor.getResource());
            final BufferedImage patternImage = bannerSprite.getTile(bannerTileMetadata.getTileX(), bannerTileMetadata.getTileY());

            final BufferedImage combined = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);

            final Graphics graphics = combined.getGraphics();
            graphics.drawImage(base, 0, 0, null);
            graphics.drawImage(patternImage, 0, 0, null);
            graphics.dispose();

            base = combined;
        }

        return this.getScaledImage(base, width, height);
    }

    private BannerSprite getBannerSprite(final String resource) throws IOException {
        if (this.spriteRegistry.containsKey(resource))
            return this.spriteRegistry.get(resource);

        final BannerSprite bannerSprite = new BannerSprite(resource, TILE_COUNT_WIDTH, TILE_COUNT_HEIGHT);
        this.spriteRegistry.put(resource, bannerSprite);

        return bannerSprite;
    }

    private BufferedImage getScaledImage(final BufferedImage src, final int w, final int h) {
        int finalw = w;
        int finalh = h;
        final double factor;

        if (src.getWidth() > src.getHeight()) {
            factor = ((double) src.getHeight() / (double) src.getWidth());
            finalh = (int) (finalw * factor);
        } else {
            factor = ((double) src.getWidth() / (double) src.getHeight());
            finalw = (int) (finalh * factor);
        }

        final BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
        final Graphics2D graphics = resizedImg.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(src, 0, 0, finalw, finalh, null);
        graphics.dispose();

        return resizedImg;
    }

    public String toMojangson() {
        final JsonObject baseObject = new JsonObject();

        // Add base color
        baseObject.addProperty("Base", this.bannerColor.getColor());

        // Add patterns
        final JsonArray jsonArray = new JsonArray();

        for (final BannerPattern bannerPattern : this.bannerPatterns) {
            final JsonObject patternObject = new JsonObject();
            patternObject.addProperty("Color", bannerPattern.getBannerColor().getColor());
            patternObject.addProperty("Pattern", bannerPattern.getBannerTileMetadata().getPattern());

            jsonArray.add(patternObject);
        }

        baseObject.add("Patterns", jsonArray);

        return GSON.toJson(baseObject).replace("\"", "");
    }

    public static Banner fromMojangson(final String mojangson) {
        return GSON.fromJson(mojangson, Banner.class);
    }
}
