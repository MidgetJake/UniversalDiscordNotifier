package universalDiscord.notifiers;

import net.runelite.api.events.ActorDeath;
import universalDiscord.message.MessageBuilder;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;

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

        MessageBuilder messageBuilder = new MessageBuilder(notifyMessage, plugin.config.deathSendImage());
        plugin.messageHandler.sendMessage(messageBuilder);

        reset();
    }

    @Override
    public boolean shouldNotify() {
        return isEnabled()
                && lastActorDeath != null
                && Objects.equals(lastActorDeath.getActor().getName(), Utils.getPlayerName());
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyDeath();
    }

    private void reset() {
        lastActorDeath = null;
    }
}
