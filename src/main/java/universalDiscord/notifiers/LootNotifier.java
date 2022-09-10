package universalDiscord.notifiers;

import net.runelite.api.ItemComposition;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.loottracker.LootReceived;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.http.api.loottracker.LootRecordType;
import universalDiscord.message.DiscordMessageBody;
import universalDiscord.message.MessageBuilder;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
        List<DiscordMessageBody.Embed> embeds = new ArrayList<>();
        StringBuilder lootMessage = new StringBuilder();
        long totalStackValue = 0;

        for (ItemStack item : Utils.reduceItemStack(receivedLoot)) {
            int itemId = item.getId();
            int quantity = item.getQuantity();
            int price = plugin.itemManager.getItemPrice(itemId);
            long totalPrice = (long) price * quantity;

            ItemComposition itemComposition = plugin.itemManager.getItemComposition(itemId);
            lootMessage.append(String.format("%s x %s (%s)\n", quantity, itemComposition.getName(), QuantityFormatter.quantityToStackSize(totalPrice)));
            if (plugin.config.lootIcons()) {
                embeds.add(new DiscordMessageBody.Embed(new DiscordMessageBody.UrlEmbed(Utils.getItemImageUrl(itemId))));
            }

            totalStackValue += totalPrice;
        }

        if (totalStackValue >= plugin.config.minLootValue()) {
            String lootString = lootMessage.toString();
            String notifyMessage = Utils.replaceCommonPlaceholders(plugin.config.lootNotifyMessage())
                    .replaceAll("%LOOT%", lootString)
                    .replaceAll("%SOURCE%", dropper)
                    .replaceAll("%TOTAL_VALUE%", QuantityFormatter.quantityToStackSize(totalStackValue))
                    .trim();

            MessageBuilder messageBuilder = new MessageBuilder(notifyMessage, plugin.config.lootSendImage(), (discordMessage) -> discordMessage.getEmbeds().addAll(embeds));
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
