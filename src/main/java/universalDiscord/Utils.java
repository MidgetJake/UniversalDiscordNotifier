package universalDiscord;

import net.runelite.api.WorldType;
import net.runelite.client.game.ItemStack;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
    public static UniversalDiscordPlugin plugin;

    public static String replaceCommonPlaceholders(String text) {
        String username = getPlayerUrl();
        if (username == null) {
            username = getPlayerName();
        } else {
            username = "[" + getPlayerName() + "](" + getPlayerUrl() + ")";
        }

        return text.replaceAll("%USERNAME%", username);
    }

    public static String getPlayerName() {
        return plugin.client.getLocalPlayer().getName();
    }

    @Nullable
    public static String getPlayerBadgeUrl() {
        String playerBadgeUrl = null;
        if (isSeasonalWorld()) {
            playerBadgeUrl = "https://oldschool.runescape.wiki/images/Leagues_chat_badge.png";
        } else if (isQuestSpeedRunningWorld()) {
            playerBadgeUrl = "https://oldschool.runescape.wiki/images/Speedrunning_World_chat_badge.png";
        } else {
            switch (plugin.client.getAccountType()) {
                case IRONMAN:
                    playerBadgeUrl = "https://oldschool.runescape.wiki/images/Ironman_chat_badge.png";
                    break;
                case HARDCORE_IRONMAN:
                    playerBadgeUrl = "https://oldschool.runescape.wiki/images/Hardcore_ironman_chat_badge.png";
                    break;
                case ULTIMATE_IRONMAN:
                    playerBadgeUrl = "https://oldschool.runescape.wiki/images/Ultimate_ironman_chat_badge.png";
                    break;
                case GROUP_IRONMAN:
                    playerBadgeUrl = "https://oldschool.runescape.wiki/images/Group_ironman_chat_badge.png";
                    break;
                case HARDCORE_GROUP_IRONMAN:
                    playerBadgeUrl = "https://oldschool.runescape.wiki/images/Hardcore_group_ironman_chat_badge.png";
                    break;
            }
        }

        return playerBadgeUrl;
    }

    public static boolean isSeasonalWorld() {
        return plugin.client.getWorldType().contains(WorldType.SEASONAL);
    }

    public static boolean isQuestSpeedRunningWorld() {
        return plugin.client.getWorldType().contains(WorldType.QUEST_SPEEDRUNNING);
    }

    public static String getPlayerUrl() {
        return plugin.config.playerUrlService().playerUrl(getPlayerName());
    }

    public static byte[] convertImageToByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Collection<ItemStack> reduceItemStack(Collection<ItemStack> items) {
        final List<ItemStack> list = new ArrayList<>();

        for (final ItemStack item : items) {
            int quantity = 0;
            for (final ItemStack i : list) {
                if (i.getId() == item.getId()) {
                    quantity = i.getQuantity();
                    list.remove(i);
                    break;
                }
            }

            if (quantity > 0) {
                list.add(new ItemStack(item.getId(), item.getQuantity() + quantity, item.getLocation()));
            } else {
                list.add(item);
            }
        }

        return list;
    }

    public static String getItemImageUrl(int itemId) {
        return "https://static.runelite.net/cache/item/icon/" + itemId + ".png";
    }

    public static String asMarkdownWikiUrl(String search) {
        return "[" + search + "](" + getWikiUrl(search) + ")";
    }

    public static String getWikiUrl(String search) {
        return "https://oldschool.runescape.wiki/w/Special:Search?search=" + URLEncoder.encode(search, StandardCharsets.UTF_8);
    }
}
