package universalDiscord.notifiers;

import net.runelite.api.ItemComposition;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.loottracker.LootReceived;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.http.api.loottracker.LootRecordType;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.message.MessageBuilder;
import universalDiscord.message.discord.Embed;
import universalDiscord.message.discord.Image;
import universalDiscord.message.discord.WebhookBody;

import javax.inject.Inject;
import java.util.Collection;


public class LootNotifier extends BaseNotifier {

    private Collection<ItemStack> receivedLoot;
    private String dropper;

    @Inject
    public LootNotifier(UniversalDiscordPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean shouldNotify() {
        return isEnabled()
                && receivedLoot != null
                && dropper != null;
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyLoot();
    }

    public void handleNotify() {
        WebhookBody webhookBody = new WebhookBody();
        StringBuilder lootMessage = new StringBuilder();
        long totalLootValue = 0;

        for (ItemStack item : Utils.reduceItemStack(receivedLoot)) {
            int itemId = item.getId();
            int quantity = item.getQuantity();
            int price = plugin.itemManager.getItemPrice(itemId);
            long itemStackPrice = (long) price * quantity;

            ItemComposition itemComposition = plugin.itemManager.getItemComposition(itemId);
            lootMessage.append(String.format("%s x %s (%s)\n", quantity, Utils.asMarkdownWikiUrl(itemComposition.getName()), QuantityFormatter.quantityToStackSize(itemStackPrice)));
            if (plugin.config.lootIcons()) {
                Embed embed = Embed.builder()
                        .image(new Image(Utils.getItemImageUrl(itemId)))
                        .build();
                webhookBody.getEmbeds().add(embed);
            }

            totalLootValue += itemStackPrice;
        }

        if (totalLootValue >= plugin.config.minLootValue()) {
            String lootString = lootMessage.toString();
            String notifyMessage = Utils.replaceCommonPlaceholders(plugin.config.lootNotifyMessage())
                    .replaceAll("%LOOT%", lootString)
                    .replaceAll("%SOURCE%", Utils.asMarkdownWikiUrl(dropper))
                    .replaceAll("%TOTAL_VALUE%", QuantityFormatter.quantityToStackSize(totalLootValue))
                    .trim();
            webhookBody.getEmbeds().add(0, Embed.builder().description(notifyMessage).build());

            MessageBuilder messageBuilder = new MessageBuilder(webhookBody, plugin.config.lootSendImage());
            plugin.messageHandler.sendMessage(messageBuilder);
        }

        reset();
    }

    public void handleNpcLootReceived(NpcLootReceived npcLootReceived) {
        dropper = npcLootReceived.getNpc().getName();
        receivedLoot = npcLootReceived.getItems();
    }

    public void handlePlayerLootReceived(PlayerLootReceived playerLootReceived) {
        dropper = playerLootReceived.getPlayer().getName();
        receivedLoot = playerLootReceived.getItems();
    }

    public void handleLootReceived(LootReceived lootReceived) {
        if (lootReceived.getType() != LootRecordType.EVENT && lootReceived.getType() != LootRecordType.PICKPOCKET) {
            return;
        }
        dropper = lootReceived.getName();
        receivedLoot = lootReceived.getItems();
    }

    private void reset() {
        dropper = null;
        receivedLoot = null;
    }
}
