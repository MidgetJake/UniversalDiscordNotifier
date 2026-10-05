package universalDiscord.notifiers;

import net.runelite.api.events.ActorDeath;
import universalDiscord.enums.DeathThumbnail;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.message.MessageBuilder;
import universalDiscord.message.discord.Image;

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
        String thumbnailUrl = deathThumbNail().getThumbnailUrl();
        if (thumbnailUrl != null) {
            messageBuilder.setFirstThumbnail(new Image(thumbnailUrl));
        }
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

    public DeathThumbnail deathThumbNail() {
        return plugin.config.deathThumbnail();
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyDeath();
    }

    private void reset() {
        lastActorDeath = null;
    }
}
