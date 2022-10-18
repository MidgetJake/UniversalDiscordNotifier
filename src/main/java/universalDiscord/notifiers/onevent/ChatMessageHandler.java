package universalDiscord.notifiers.onevent;

import net.runelite.api.events.ChatMessage;
import universalDiscord.notifiers.Notifier;

public interface ChatMessageHandler extends Notifier {
    void handleChatMessage(ChatMessage chatMessage) ;
}
