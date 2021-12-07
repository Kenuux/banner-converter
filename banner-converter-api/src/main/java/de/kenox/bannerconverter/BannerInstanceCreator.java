package de.kenox.bannerconverter;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

/**
 * @author Kenox
 */
public class BannerInstanceCreator implements InstanceCreator<ConvertedBanner> {

    @Override
    public ConvertedBanner createInstance(final Type type) {
        return new ConvertedBanner(null, null);
    }
}
