package de.kenox.bannerconverter.spigot;

import de.kenox.bannerconverter.BannerStandaloneApi;
import de.kenox.bannerconverter.BannerUtil;
import de.kenox.bannerconverter.ConvertedBanner;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Kenox
 */
public class BannerSpigotApi {

    private static final String PACKAGENAME = Bukkit.getServer().getClass().getPackage().getName();
    private static final String VERSION = PACKAGENAME.substring(PACKAGENAME.lastIndexOf(".") + 1);

    private static final BannerStandaloneApi bannerStandaloneApi = new BannerStandaloneApi();

    public Optional<BufferedImage> getBannerImage(final ItemStack banner, final int width, final int height) {
        if (!(banner.getItemMeta() instanceof BannerMeta bannerMeta))
            throw new IllegalArgumentException("Banner item should have a banner meta");

        return this.getBannerImage(banner.getData().getData(), bannerMeta.getPatterns(), width, height);
    }

    public Optional<BufferedImage> getBannerImage(final Banner banner, final int width, final int height) {
        return this.getBannerImage(banner.getBaseColor().getDyeData(), banner.getPatterns(), width, height);
    }

    private Optional<BufferedImage> getBannerImage(final byte baseColor, final List<Pattern> patterns, final int width, final int height) {
        final String mojangson = BannerUtil.toMojangson(baseColor, this.patternsToColors(patterns),
                this.patternsToIdentifiers(patterns));

        return bannerStandaloneApi.getBannerImage(mojangson, width, height);
    }

    public ConvertedBanner getRandomBanner(final int maxPatternDepth) {
        return bannerStandaloneApi.getRandomBanner(maxPatternDepth);
    }

    public ItemStack createItemStack(final ConvertedBanner convertedBanner) {
        String mojangson = convertedBanner.toMojangson();

        if (!mojangson.matches("^\\{\\s*BlockEntityTag\\s*:.*"))
            mojangson = "{BlockEntityTag: " + mojangson + "}";

        final ItemStack item = new ItemStack(this.baseColorToBanner(convertedBanner.getBannerColor().getColor()), 1);

        final boolean nbtPackageChanged = this.nbtPackageChanged();
        try {
            final Class<?> nmsItemStackClass = Class.forName(nbtPackageChanged ? "net.minecraft.world.item.ItemStack" : "net.minecraft.server." + VERSION + ".ItemStack");
            final Class<?> compoundClass = Class.forName(nbtPackageChanged ? "net.minecraft.nbt.NBTTagCompound" : "net.minecraft.server." + VERSION + ".NBTTagCompound");
            final Class<?> mojangsonParserClass = Class.forName(nbtPackageChanged ? "net.minecraft.nbt.MojangsonParser" : "net.minecraft.server." + VERSION + ".MojangsonParser");
            final Method parseMethod = mojangsonParserClass.getMethod("parse", String.class);
            Object nbtTagCompound = parseMethod.invoke(mojangsonParserClass, mojangson);
            if (nbtTagCompound == null) {
                nbtTagCompound = compoundClass.newInstance();
            }

            final Class<?> craftItemStack = Class
                    .forName("org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack");
            final Method asNMSCopy = craftItemStack.getMethod("asNMSCopy", ItemStack.class);
            final Object nmsItemStack = asNMSCopy.invoke(asNMSCopy, item);

            final Method setTag = nmsItemStackClass.getMethod("setTag", compoundClass);
            setTag.invoke(nmsItemStack, nbtTagCompound);
            final Method asBukkitCopy = craftItemStack.getMethod("asBukkitCopy", nmsItemStackClass);
            return (ItemStack) asBukkitCopy.invoke(asBukkitCopy, nmsItemStack);
        } catch (final Exception var5) {
            var5.printStackTrace();
            return item;
        }
    }

    private Material baseColorToBanner(final byte color) {
        return this.bannerMaterialChanged() ? (Material) Enum.valueOf((Class) Material.class, DyeColor.getByDyeData(color).name() + "_BANNER") : (Material) Enum.valueOf((Class) Material.class, "BANNER");
    }

    private List<Byte> patternsToColors(final List<Pattern> patterns) {
        return patterns.stream()
                .map(x -> x.getColor().getDyeData()).collect(Collectors.toList());
    }

    private List<String> patternsToIdentifiers(final List<Pattern> patterns) {
        return patterns.stream()
                .map(x -> x.getPattern().getIdentifier()).collect(Collectors.toList());
    }

    private int getMinorVersion() {
        final String bukkitVersion = Bukkit.getBukkitVersion();
        final String major = bukkitVersion.split("-")[0];
        final int minor = Integer.parseInt(major.split("\\.")[1]);
        return minor;
    }

    private boolean nbtPackageChanged() {
        return this.getMinorVersion() >= 17;
    }

    private boolean bannerMaterialChanged() {
        return this.getMinorVersion() >= 13;
    }
}
