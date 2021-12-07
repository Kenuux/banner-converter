package de.kenox.bannerconverter.example;

import de.kenox.bannerconverter.ConvertedBanner;
import de.kenox.bannerconverter.spigot.BannerSpigotApi;
import org.bukkit.Bukkit;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Kenox
 */
public class ExamplePlugin extends JavaPlugin implements Listener, CommandExecutor {

    private static final String PREFIX = "§7[§2BannerApi§7]";
    private final BannerSpigotApi bannerSpigotApi = new BannerSpigotApi();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("banner").setExecutor(this);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        final Block block = event.getClickedBlock();

        if (block == null)
            return;

        final BlockState blockState = block.getState();

        if (!(blockState instanceof Banner banner))
            return;

        final Optional<BufferedImage> optionalBufferedImage = this.bannerSpigotApi.getBannerImage(banner, 320, 160);

        if (optionalBufferedImage.isEmpty()) {
            player.sendMessage(PREFIX + " §cSomething went wrong.");
            return;
        }

        player.sendMessage(PREFIX + " §aShould be saved: " + this.saveBanner(optionalBufferedImage.get()));
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player player))
            return false;

        if (args.length == 0) {
            player.sendMessage(PREFIX + " §c/banner save");
            player.sendMessage(PREFIX + " §c/banner random <depth>");
            return true;
        }

        if (args[0].equals("save")) {
            final ItemStack banner = player.getItemInHand();

            if (!banner.getType().name().toUpperCase().contains("BANNER")) {
                player.sendMessage(PREFIX + " §cYou must have a banner in your main hand!");
                return false;
            }

            final Optional<BufferedImage> optionalBufferedImage = this.bannerSpigotApi.getBannerImage(banner, 320, 160);

            if (optionalBufferedImage.isEmpty()) {
                player.sendMessage(PREFIX + " §cSomething went wrong.");
                return false;
            }

            player.sendMessage(PREFIX + " §aShould be saved: " + this.saveBanner(optionalBufferedImage.get()));
            return true;
        }

        if (args[0].equalsIgnoreCase("random") && args.length == 2) {
            if (!args[1].matches("\\d+"))
                return false;

            final int depth = Integer.parseInt(args[1]);

            if (depth <= 0 || depth > 6) {
                player.sendMessage(PREFIX + " §cAs a banner can only have up to 6 patterns, please choose the depth respectively.");
                return false;
            }

            final ConvertedBanner convertedBanner = this.bannerSpigotApi.getRandomBanner(depth);

            final ItemStack itemStack = this.bannerSpigotApi.createItemStack(convertedBanner);

            player.getInventory().addItem(itemStack);

            player.sendMessage(PREFIX + " §aBanner with pattern depth " + depth + " created.");
        }

        return true;
    }

    private String saveBanner(final BufferedImage bufferedImage) {
        final File outputfile = new File("plugins/Banners/" + UUID.randomUUID().toString().split("-")[0] + ".png");
        outputfile.mkdirs();
        try {
            if (!outputfile.exists())
                outputfile.createNewFile();
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return outputfile.getName();
    }
}
