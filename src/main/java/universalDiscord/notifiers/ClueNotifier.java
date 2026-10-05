package universalDiscord.notifiers;

import net.runelite.api.ItemComposition;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;
import universalDiscord.enums.ClueType;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.message.MessageBuilder;
import universalDiscord.message.discord.Embed;
import universalDiscord.message.discord.Image;
import universalDiscord.message.discord.WebhookBody;
import universalDiscord.notifiers.onevent.ChatMessageHandler;
import universalDiscord.notifiers.onevent.WidgetLoadHandler;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClueNotifier extends BaseNotifier implements ChatMessageHandler, WidgetLoadHandler {
    private static final Pattern CLUE_SCROLL_REGEX = Pattern.compile("You have completed (?<scrollCount>\\d+) (?<scrollType>\\w+) Treasure Trails\\.");

    private Matcher lastClueMatcher;
    public HashMap<Integer, Integer> clueItems = new HashMap<>();

    public ClueNotifier(UniversalDiscordPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean shouldNotify() {
        return isEnabled()
                // Filled by onWidgetLoaded()
                && clueItems.size() > 0
                // Set by HandleNewChatMessage()
                && lastClueMatcher != null;
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyClue();
    }

    public void handleNotify() {
        WebhookBody webhookBody = new WebhookBody();
        StringBuilder lootMessage = new StringBuilder();
        long totalClueValue = 0;

        for (Integer itemId : clueItems.keySet()) {
            int quantity = clueItems.get(itemId);
            int price = plugin.itemManager.getItemPrice(itemId);
            long itemStackPrice = (long) price * quantity;
            totalClueValue += itemStackPrice;
            ItemComposition itemComposition = plugin.itemManager.getItemComposition(itemId);

            if (plugin.config.clueShowItems()) {
                Embed embed = Embed.builder()
                        .image(new Image(Utils.getItemImageUrl(itemId)))
                        .build();
                webhookBody.getEmbeds().add(embed);
            }
            String itemText = String.format("%s x %s (%s)\n", quantity, Utils.asMarkdownWikiUrl(itemComposition.getName()), QuantityFormatter.quantityToStackSize(itemStackPrice));
            lootMessage.append(itemText);
        }

        if (totalClueValue > plugin.config.clueMinValue()) {
            ClueType clueType = ClueType.valueOf(lastClueMatcher.group("scrollType").trim().toUpperCase());

            String notifyMessage = Utils.replaceCommonPlaceholders(plugin.config.clueNotifyMessage())
                    .replaceAll("%CLUE%", clueType.getMarkdownWikiUrl())
                    .replaceAll("%COUNT%", lastClueMatcher.group("scrollCount"))
                    .replaceAll("%TOTAL_VALUE%", QuantityFormatter.quantityToStackSize(totalClueValue))
                    .replaceAll("%LOOT%", lootMessage.toString().trim());


            webhookBody.getEmbeds().add(0, Embed.builder()
                    .description(notifyMessage)
                    .thumbnail(clueType.getCasketImage())
                    .build());

            MessageBuilder messageBuilder = new MessageBuilder(webhookBody, plugin.config.clueSendImage());
            plugin.messageHandler.sendMessage(messageBuilder);
        }

        reset();
    }

    @Override
    public void handleChatMessage(ChatMessage chatMessage) {
        String cleanedMessage = Text.removeTags(chatMessage.getMessage());
        Matcher clueMatcher = CLUE_SCROLL_REGEX.matcher(cleanedMessage);
        if (clueMatcher.find()) {
            lastClueMatcher = clueMatcher;
        }
    }

    private void reset() {
        lastClueMatcher = null;
        clueItems.clear();
    }

    @Override
    public void handleWidgetLoaded(WidgetLoaded widgetLoaded) {
        if (widgetLoaded.getGroupId() == WidgetID.CLUE_SCROLL_REWARD_GROUP_ID) {
            Widget clue = plugin.client.getWidget(WidgetInfo.CLUE_SCROLL_REWARD_ITEM_CONTAINER);
            if (clue != null) {
                clueItems.clear();
                Widget[] children = clue.getChildren();

                if (children == null) {
                    return;
                }

                for (Widget child : children) {
                    if (child == null) {
                        continue;
                    }

                    int quantity = child.getItemQuantity();
                    int itemId = child.getItemId();

                    if (itemId > -1 && quantity > 0) {
                        clueItems.put(itemId, quantity);
                    }
                }
            }
        }
    }
}
