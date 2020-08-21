package me.kenox.bannerconverter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author Kenox
 */
@RequiredArgsConstructor
public enum BannerTileMetadata {

    // 1st row
    BASE("base", 0, 0),
    RECTANGLE_BOTTOM_LEFT("bl", 160, 0),
    FRAME("bo", 320, 0),
    RECTANGLE_BOTTOM_RIGHT("br", 480, 0),
    BRICKS("bri", 640, 0),

    // 2nd row
    VERTICAL_QUARTER_DOWN("bs", 0, 320),
    SPIKE_BOTTOM("bt", 160, 320),
    TRIPLE_SPIKES_BOTTOM("bts", 320, 320),
    BORDURE_INDENTED("cbo", 480, 320),
    DIAGONAL_CROSS("cr", 640, 320),

    // 3rd row
    CREEPER("cre", 0, 640),
    VERTICAL_QUARTER_MID("cs", 160, 640),
    DIAGONAL_TOP_RIGHT("dls", 320, 640),
    DIAGONAL_TOP_LEFT("drs", 480, 640),
    FLOWER_CHARGE("flo", 640, 640),

    // 4th row
    GRADIENT_DOWN("gra", 0, 960),
    VERTICAL_HALF_TOP("hh", 160, 960),
    TRIANGLE_TOP_LEFT("ld", 320, 960),
    HORIZONTAL_QUARTER_LEFT("ls", 480, 960),
    CIRCLE("mc", 640, 960),

    // 5th row
    MOJANG("moj", 0, 1280),
    DIAMOND("mr", 160, 1280),
    HORIZONTAL_QUARTER_MID("ms", 320, 1280),
    TRIANGLE_TOP_RIGHT("rud", 480, 1280),
    HORIZONTAL_QUARTER_RIGHT("rs", 640, 1280),

    // 6th row
    HORIZONTAL_VERTICAL_CROSS("sc", 0, 1600),
    SKELETON("sku", 160, 1600),
    STRIPES("ss", 320, 1600),
    RECTANGLE_TOP_LEFT("tl", 480, 1600),
    RECTANGLE_TOP_RIGHT("tr", 640, 1600),

    // 7th row
    VERTICAL_QUARTER_TOP("ts", 0, 1920),
    SPIKE_TOP("tt", 160, 1920),
    TRIPLE_SPIKES_TOP("tts", 320, 1920),
    HORIZONTAL_HALF_LEFT("vh", 480, 1920),
    GRADIENT_UP("gru", 640, 1920),

    // 8th row
    HORIZONTAL_HALF_RIGHT("vhr", 0, 2240),
    VERTICAL_HALF_DOWN("hhb", 160, 2240),
    TRIANGLE_BOTTOM_LEFT("lud", 320, 2240),
    TRIANGLE_BOTTOM_RIGHT("rd", 480, 2240);

    @Getter private final String pattern;
    @Getter private final int tileX;
    @Getter private final int tileY;

    public static BannerTileMetadata getTileMetadataByPattern(final String pattern) {
        return Arrays.stream(values())
                .filter(bannerTileMetadata -> bannerTileMetadata.getPattern().equals(pattern))
                .findFirst()
                .orElse(BannerTileMetadata.BASE);
    }
}
