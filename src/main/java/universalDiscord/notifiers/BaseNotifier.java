package universalDiscord.notifiers;

import universalDiscord.UniversalDiscordPlugin;

import javax.inject.Inject;

public abstract class BaseNotifier implements Notifier {
    protected final UniversalDiscordPlugin plugin;

    @Inject
    public BaseNotifier(UniversalDiscordPlugin plugin) {
        this.plugin = plugin;
    }

    public void handleNotify() {
        plugin.messageHandler.createMessage("This is a base notification", false, null);
    }

    /**
     * @return True when all data is gathered and the notification is enabled.
     */
    public abstract boolean shouldNotify();

    public abstract boolean isEnabled();
}
