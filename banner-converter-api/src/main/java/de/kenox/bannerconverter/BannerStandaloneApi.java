package de.kenox.bannerconverter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Kenox
 */
public class BannerStandaloneApi {

    private final Cache<StoredBanner, BufferedImage> BANNER_IMAGES = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    private Optional<StoredBanner> getStoredBanner(final String mojangson, final int width, final int height) {
        return BANNER_IMAGES.asMap().keySet().stream()
                .filter(storedBanner -> storedBanner.mojangson.equals(mojangson)
                        && storedBanner.width == width && storedBanner.height == height)
                .findFirst();
    }

    public Optional<BufferedImage> getBannerImage(final String json, final int width, final int height) {
        final Optional<StoredBanner> storedBannerOptional = getStoredBanner(json, width, height);
        if (storedBannerOptional.isPresent())
            return Optional.of(BANNER_IMAGES.asMap().get(storedBannerOptional.get()));

        try {
            final BufferedImage bufferedImage = ConvertedBanner.fromJson(json).toImage(width, height);
            BANNER_IMAGES.put(new StoredBanner(json, width, height), bufferedImage);
            return Optional.of(bufferedImage);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public ConvertedBanner getRandomBanner(final int maxPatternDepth) {
        if (maxPatternDepth <= 0 || maxPatternDepth > 6)
            throw new IllegalArgumentException("As a banner can only have up to 6 patterns (minimum 1 pattern), please choose the depth respectively");

        final BannerColor baseColor = getRandomBannerColor();
        final List<BannerPattern> bannerPatterns = new ArrayList<>();

        final int patternDepth = ThreadLocalRandom.current().nextInt(maxPatternDepth - 1) + 1;
        for (int i = 0; i < patternDepth; i++) {
            final BannerColor patternColor = getRandomBannerColor();
            final BannerTileMetadata pattern = getRandomBannerTileMetadata();

            bannerPatterns.add(new BannerPattern(patternColor, pattern));
        }

        return new ConvertedBanner(baseColor, bannerPatterns);
    }

    private static BannerColor getRandomBannerColor() {
        final BannerColor[] bannerColors = BannerColor.values();
        return bannerColors[ThreadLocalRandom.current().nextInt(bannerColors.length)];
    }

    private static BannerTileMetadata getRandomBannerTileMetadata() {
        final BannerTileMetadata[] bannerTileMetadata = BannerTileMetadata.values();
        return bannerTileMetadata[ThreadLocalRandom.current().nextInt(bannerTileMetadata.length)];
    }

    @Data
    private static class StoredBanner {

        private final String mojangson;
        private final int width;
        private final int height;
    }
}
