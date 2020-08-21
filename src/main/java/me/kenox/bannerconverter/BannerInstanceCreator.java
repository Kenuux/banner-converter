package me.kenox.bannerconverter;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

/**
 * @author Kenox
 */
public class BannerInstanceCreator implements InstanceCreator<Banner> {

    @Override
    public Banner createInstance(final Type type) {
        return new Banner(null, null);
    }
}
