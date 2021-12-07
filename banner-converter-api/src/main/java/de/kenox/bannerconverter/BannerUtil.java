package de.kenox.bannerconverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * @author Kenox
 */
public class BannerUtil {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ConvertedBanner.class, new BannerInstanceCreator())
            .registerTypeAdapter(BannerColor.class, new BannerColorDeserializer())
            .registerTypeAdapter(BannerTileMetadata.class, new BannerTileMetadataDeserializer())
            .create();

    public static String toMojangson(final int baseColor, final List<Byte> patternDyeColors, final List<String> patternIdentifiers) {
        final JsonObject baseObject = new JsonObject();

        // Add base color
        baseObject.addProperty("Base", baseColor);

        // Add patterns
        final JsonArray jsonArray = new JsonArray();

        for (int i = 0; i < patternDyeColors.size(); i++) {
            final byte dyeColor = patternDyeColors.get(i);
            final String identifier = patternIdentifiers.get(i);

            final JsonObject patternObject = new JsonObject();
            patternObject.addProperty("Color", dyeColor);
            patternObject.addProperty("Pattern", identifier);

            jsonArray.add(patternObject);
        }

        baseObject.add("Patterns", jsonArray);

        return GSON.toJson(baseObject).replace("\"", "");
    }
}
