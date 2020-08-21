package me.kenox.bannerconverter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author Kenox
 */
@RequiredArgsConstructor
public enum BannerColor {

    BLACK("Black", 0, "assets/black.png"),
    DARK_GRAY("Dark Gray", 8, "assets/dark_gray.png"),
    LIGHT_GRAY("Light Gray", 7, "assets/gray.png"),
    WHITE("White", 15, "assets/white.png"),
    PINK("Pink", 9, "assets/pink.png"),
    MAGENTA("Magenta", 13, "assets/magenta.png"),
    PURPLE("Purple", 5, "assets/purple.png"),
    BLUE("Blue", 4, "assets/blue.png"),
    CYAN("Cyan", 6, "assets/cyan.png"),
    LIGHT_BLUE("Light Blue", 12, "assets/light_blue.png"),
    GREEN("Green", 2, "assets/green.png"),
    LIME("Lime", 10, "assets/lime.png"),
    YELLOW("Yellow", 11, "assets/yellow.png"),
    ORANGE("Orange", 14, "assets/orange.png"),
    BROWN("Brown", 13, "assets/brown.png"),
    RED("Red", 1, "assets/red.png");

    @Getter private final String colorString;
    @Getter private final int color;
    @Getter private final String resource;

    public static BannerColor getColorByInt(final int color) {
        return Arrays.stream(values())
                .filter(bannerColor -> bannerColor.getColor() == color)
                .findFirst()
                .orElse(BannerColor.BLACK);
    }
}
