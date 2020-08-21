package me.kenox.bannerconverter;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author Kenox
 */
@Data
public class BannerPattern {

    @SerializedName("Color")
    private final BannerColor bannerColor;

    @SerializedName("Pattern")
    private final BannerTileMetadata bannerTileMetadata;
}
