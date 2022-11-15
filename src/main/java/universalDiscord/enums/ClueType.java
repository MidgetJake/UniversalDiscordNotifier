package universalDiscord.enums;

import net.runelite.api.ItemID;
import universalDiscord.Utils;
import universalDiscord.message.discord.Image;

@SuppressWarnings("unused")
public enum ClueType implements ThumbnailUrl {
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
        return new Image(this.getThumbnailUrl());
    }

    public String getMarkdownWikiUrl() {
        return Utils.asMarkdownWikiUrl(this.name().toLowerCase().concat(" clue"), this.name().toLowerCase());
    }

    @Override
    public String getThumbnailUrl() {
        return Utils.getItemImageUrl(casketItemId);
    }
}
