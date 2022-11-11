package universalDiscord;

import net.runelite.api.ItemID;
import universalDiscord.message.discord.Image;

public enum ClueType {
    BEGINNER(ItemID.REWARD_CASKET_BEGINNER),
    EASY(ItemID.REWARD_CASKET_EASY),
    MEDIUM(ItemID.REWARD_CASKET_MEDIUM),
    HARD(ItemID.REWARD_CASKET_HARD),
    ELITE(ItemID.REWARD_CASKET_ELITE),
    MASTER(ItemID.REWARD_CASKET_MASTER);

    final public int casketItemId;

    ClueType(int casketItemId) {
        this.casketItemId = casketItemId;
    }

    public Image getCasketImage() {
        return new Image(Utils.getItemImageUrl(casketItemId));
    }

    public String getMarkdownWikiUrl() {
        return Utils.asMarkdownWikiUrl(this.name().toLowerCase().concat(" clue"), this.name().toLowerCase());
    }
}
