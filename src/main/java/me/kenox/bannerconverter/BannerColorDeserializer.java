package me.kenox.bannerconverter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author Kenox
 */
public class BannerColorDeserializer implements JsonDeserializer<BannerColor> {

    @Override
    public BannerColor deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return BannerColor.getColorByInt(jsonElement.getAsInt());
    }
}
