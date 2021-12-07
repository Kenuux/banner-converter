package de.kenox.bannerconverter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author Kenox
 */
@RequiredArgsConstructor
public enum BannerColor {

    BLACK("Black", (byte) 0, "assets/black.png"),
    DARK_GRAY("Dark Gray", (byte) 8, "assets/dark_gray.png"),
    LIGHT_GRAY("Light Gray", (byte) 7, "assets/gray.png"),
    WHITE("White", (byte) 15, "assets/white.png"),
    PINK("Pink", (byte) 9, "assets/pink.png"),
    MAGENTA("Magenta", (byte) 13, "assets/magenta.png"),
    PURPLE("Purple", (byte) 5, "assets/purple.png"),
    BLUE("Blue", (byte) 4, "assets/blue.png"),
    CYAN("Cyan", (byte) 6, "assets/cyan.png"),
    LIGHT_BLUE("Light Blue", (byte) 12, "assets/light_blue.png"),
    GREEN("Green", (byte) 2, "assets/green.png"),
    LIME("Lime", (byte) 10, "assets/lime.png"),
    YELLOW("Yellow", (byte) 11, "assets/yellow.png"),
    ORANGE("Orange", (byte) 14, "assets/orange.png"),
    BROWN("Brown", (byte) 13, "assets/brown.png"),
    RED("Red", (byte) 1, "assets/red.png");

    @Getter
    private final String colorString;
    @Getter
    private final byte color;
    @Getter
    private final String resource;

    public static BannerColor getColorByInt(final byte color) {
        return Arrays.stream(values())
                .filter(bannerColor -> bannerColor.getColor() == color)
                .findFirst()
                .orElse(BannerColor.BLACK);
    }
}
