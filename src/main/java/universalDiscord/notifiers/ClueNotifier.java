package universalDiscord.notifiers;

import net.runelite.api.ItemComposition;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;
import universalDiscord.DiscordMessageBody;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.notifiers.onevent.ChatMessageHandler;
import universalDiscord.notifiers.onevent.WidgetLoadHandler;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClueNotifier extends BaseNotifier implements ChatMessageHandler, WidgetLoadHandler {
    private static final Pattern CLUE_SCROLL_REGEX = Pattern.compile("You have completed (?<scrollCount>\\d+) (?<scrollType>\\w+) Treasure Trails\\.");

    private Matcher lastClueMatcher;
    public HashMap<Integer, Integer> clueItems = new HashMap<>();

    private DiscordMessageBody messageBody;

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
        messageBody = new DiscordMessageBody();
        StringBuilder lootMessage = new StringBuilder();
        long totalPrice = 0;

        for (Integer itemId : clueItems.keySet()) {
            if (lootMessage.length() > 0) {
                lootMessage.append("\n");
            }
            int quantity = clueItems.get(itemId);
            int price = plugin.itemManager.getItemPrice(itemId);
            totalPrice += (long) price * quantity;
            lootMessage.append(getItem(itemId, clueItems.get(itemId)));
        }

        if (totalPrice < plugin.config.clueMinValue()) {
            return;
        }

        String notifyMessage = plugin.config.clueNotifyMessage()
                .replaceAll("%USERNAME%", Utils.getPlayerName())
                .replaceAll("%CLUE%", lastClueMatcher.group("scrollType"))
                .replaceAll("%COUNT%", lastClueMatcher.group("scrollCount"))
                .replaceAll("%LOOT%", lootMessage.toString());
        plugin.messageHandler.createMessage(notifyMessage, plugin.config.clueSendImage(), null);

        reset();
    }

    public String getItem(int itemId, int quantity) {
        int price = plugin.itemManager.getItemPrice(itemId);
        long totalPrice = (long) price * quantity;
        ItemComposition itemComposition = plugin.itemManager.getItemComposition(itemId);

        if (plugin.config.clueShowItems()) {
            messageBody.getEmbeds().add(new DiscordMessageBody.Embed(new DiscordMessageBody.UrlEmbed(Utils.getItemImageUrl(itemId))));
        }
        return String.format("%s x %s (%s)", quantity, itemComposition.getName(), QuantityFormatter.quantityToStackSize(totalPrice));
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
