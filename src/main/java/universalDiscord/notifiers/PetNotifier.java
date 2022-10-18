package universalDiscord.notifiers;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.message.MessageBuilder;
import universalDiscord.notifiers.onevent.ChatMessageHandler;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetNotifier extends BaseNotifier implements ChatMessageHandler {
    private static final Pattern PET_REGEX = Pattern.compile("You have a funny feeling like you.*");

    private Matcher lastPetMatcher;

    @Inject
    public PetNotifier(UniversalDiscordPlugin plugin) {
        super(plugin);
    }

    @Override
    public void handleNotify() {
        String notifyMessage = Utils.replaceCommonPlaceholders(plugin.config.petNotifyMessage());
        MessageBuilder messageBuilder = new MessageBuilder(notifyMessage, plugin.config.petSendImage());
        plugin.messageHandler.sendMessage(messageBuilder);

        reset();
    }


    @Override
    public boolean shouldNotify() {
        return isEnabled() && lastPetMatcher != null;
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyPet();
    }

    public void handleChatMessage(ChatMessage chatMessage) {
        String cleanedMessage = Text.removeTags(chatMessage.getMessage());
        Matcher matcher = PET_REGEX.matcher(cleanedMessage);
        if (matcher.find()) {
            lastPetMatcher = matcher;
        }
    }

    private void reset() {
        lastPetMatcher = null;
    }
}
