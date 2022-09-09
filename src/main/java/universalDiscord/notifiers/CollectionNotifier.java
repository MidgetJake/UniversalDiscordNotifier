package universalDiscord.notifiers;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;
import universalDiscord.message.MessageBuilder;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.notifiers.onevent.ChatMessageHandler;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CollectionNotifier extends BaseNotifier implements ChatMessageHandler {
    private static final Pattern COLLECTION_LOG_REGEX = Pattern.compile("New item added to your collection log: (?<itemName>[\\w,\\s-.]+)");

    private Matcher lastMatcher;

    @Inject
    public CollectionNotifier(UniversalDiscordPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean shouldNotify() {
        return isEnabled() && lastMatcher != null;
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyCollectionLog();
    }

    public void handleNotify() {
        String notifyMessage = Utils.replaceCommonPlaceholders(plugin.config.collectionNotifyMessage())
                .replaceAll("%ITEM%", lastMatcher.group("itemName"));

        MessageBuilder messageBuilder = new MessageBuilder(notifyMessage, plugin.config.collectionSendImage());
        plugin.messageHandler.sendMessage(messageBuilder);

        reset();
    }

    public void handleChatMessage(ChatMessage chatMessage) {
        String cleanedMessage = Text.removeTags(chatMessage.getMessage());
        Matcher matcher = COLLECTION_LOG_REGEX.matcher(cleanedMessage);
        if (matcher.find()) {
            lastMatcher = matcher;
        }
    }

    private void reset() {
        lastMatcher = null;
    }
}
