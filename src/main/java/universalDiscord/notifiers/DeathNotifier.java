package universalDiscord.notifiers;

import net.runelite.api.Player;
import net.runelite.api.events.ActorDeath;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.message.MessageBuilder;

import javax.inject.Inject;
import java.util.Objects;

public class DeathNotifier extends BaseNotifier {
    public ActorDeath lastActorDeath = null;

    @Inject
    public DeathNotifier(UniversalDiscordPlugin plugin) {
        super(plugin);
    }

    @Override
    public void handleNotify() {
        String notifyMessage = Utils.replaceCommonPlaceholders(plugin.config.deathNotifyMessage());

        MessageBuilder messageBuilder = MessageBuilder.textAsEmbed(notifyMessage, plugin.config.deathSendImage());
        plugin.messageHandler.sendMessage(messageBuilder);

        reset();
    }

    @Override
    public boolean shouldNotify() {
        return isEnabled()
                && lastActorDeathIsLocalPlayer();
    }

    public boolean lastActorDeathIsLocalPlayer() {
        if (lastActorDeath != null) {
            return Objects.equals(lastActorDeath.getActor(), plugin.client.getLocalPlayer());
        }

        return false;
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyDeath();
    }

    private void reset() {
        lastActorDeath = null;
    }
}
