package universalDiscord;

import com.google.inject.Guice;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NotificationFired;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.loottracker.LootReceived;
import net.runelite.client.ui.DrawManager;
import okhttp3.OkHttpClient;
import universalDiscord.message.DiscordMessageHandler;
import universalDiscord.notifiers.*;
import universalDiscord.notifiers.onevent.ChatMessageHandler;
import universalDiscord.notifiers.onevent.WidgetLoadHandler;

import javax.inject.Inject;


@Slf4j
@PluginDescriptor(
        name = "Universal Discord"
)
public class UniversalDiscordPlugin extends Plugin {
    @Inject
    public Client client;
    @Inject
    public OkHttpClient httpClient;
    @Inject
    public DrawManager drawManager;
    @Inject
    public UniversalDiscordConfig config;
    @Inject
    public ItemManager itemManager;

    @Inject
    public ItemSearcher itemSearcher;

    public final DiscordMessageHandler messageHandler = new DiscordMessageHandler(this);
    private final CollectionNotifier collectionNotifier = new CollectionNotifier(this);
    private final PetNotifier petNotifier = new PetNotifier(this);
    private final LevelNotifier levelNotifier = new LevelNotifier(this);
    private final LootNotifier lootNotifier = new LootNotifier(this);
    private final DeathNotifier deathNotifier = new DeathNotifier(this);
    private final SlayerNotifier slayerNotifier = new SlayerNotifier(this);
    private final QuestNotifier questNotifier = new QuestNotifier(this);
    private final ClueNotifier clueNotifier = new ClueNotifier(this);


    @Override
    protected void startUp() {
        Utils.plugin = this;
        itemSearcher.loadItemIdsAndNames();

        log.info("Started up Universal Discord");
    }

    @Override
    protected void shutDown() {
        log.info("Shutting down Universal Discord");
    }

    @Provides
    UniversalDiscordConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(UniversalDiscordConfig.class);
    }

    @Subscribe
    public void onNotificationFired(NotificationFired notif) {

    }

    @Subscribe
    public void onUsernameChanged(UsernameChanged usernameChanged) {
        levelNotifier.reset();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState().equals(GameState.LOGIN_SCREEN)) {
            levelNotifier.reset();
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged statChange) {
        levelNotifier.handleLevelUp(statChange.getSkill().getName(), statChange.getLevel());
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        levelNotifier.onTick();
    }

    @Subscribe
    public void onChatMessage(ChatMessage message) {
        ChatMessageType msgType = message.getType();

        if (msgType.equals(ChatMessageType.GAMEMESSAGE)) {
            ChatMessageHandler[] notifiers = new ChatMessageHandler[]{
                    collectionNotifier,
                    petNotifier,
                    slayerNotifier,
                    clueNotifier,
            };
            for (ChatMessageHandler notifier : notifiers) {
                notifier.handleChatMessage(message);
                if (notifier.shouldNotify()) {
                    notifier.handleNotify();
                }
            }
        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath actor) {
        deathNotifier.lastActorDeath = actor;
        if (deathNotifier.shouldNotify()) {
            deathNotifier.handleNotify();
        }
    }

    @Subscribe
    public void onNpcLootReceived(NpcLootReceived npcLootReceived) {
        lootNotifier.handleNpcLootReceived(npcLootReceived);
        if (lootNotifier.shouldNotify()) {
            lootNotifier.handleNotify();
        }
    }

    @Subscribe
    public void onPlayerLootReceived(PlayerLootReceived playerLootReceived) {
        lootNotifier.handlePlayerLootReceived(playerLootReceived);
        if (lootNotifier.shouldNotify()) {
            lootNotifier.handleNotify();
        }
    }

    @Subscribe
    public void onLootReceived(LootReceived lootReceived) {
        lootNotifier.handleLootReceived(lootReceived);
        if (lootNotifier.shouldNotify()) {
            lootNotifier.handleNotify();
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        WidgetLoadHandler[] notifiers = new WidgetLoadHandler[]{
                questNotifier,
                clueNotifier,
        };

        for (WidgetLoadHandler notifier : notifiers) {
            notifier.handleWidgetLoaded(event);
            if (notifier.shouldNotify()) {
                notifier.handleNotify();
            }
        }
    }
}
