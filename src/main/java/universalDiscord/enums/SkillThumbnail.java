package universalDiscord.enums;

@SuppressWarnings("unused")
public enum SkillThumbnail implements ThumbnailUrl {
    ATTACK("https://oldschool.runescape.wiki/images/Attack_icon_%28detail%29.png"),
    DEFENCE("https://oldschool.runescape.wiki/images/Defence_icon_%28detail%29.png"),
    STRENGTH("https://oldschool.runescape.wiki/images/Strength_icon_%28detail%29.png"),
    HITPOINTS("https://oldschool.runescape.wiki/images/Hitpoints_icon_%28detail%29.png"),
    RANGED("https://oldschool.runescape.wiki/images/Ranged_icon_%28detail%29.png"),
    PRAYER("https://oldschool.runescape.wiki/images/Prayer_icon_%28detail%29.png"),
    MAGIC("https://oldschool.runescape.wiki/images/Magic_icon_%28detail%29.png"),
    COOKING("https://oldschool.runescape.wiki/images/Cooking_icon_%28detail%29.png"),
    WOODCUTTING("https://oldschool.runescape.wiki/images/Woodcutting_icon_%28detail%29.png"),
    FLETCHING("https://oldschool.runescape.wiki/images/Fletching_icon_%28detail%29.png"),
    FISHING("https://oldschool.runescape.wiki/images/Fishing_icon_%28detail%29.png"),
    FIREMAKING("https://oldschool.runescape.wiki/images/Firemaking_icon_%28detail%29.png"),
    CRAFTING("https://oldschool.runescape.wiki/images/Crafting_icon_%28detail%29.png"),
    SMITHING("https://oldschool.runescape.wiki/images/Smithing_icon_%28detail%29.png"),
    MINING("https://oldschool.runescape.wiki/images/Mining_icon_%28detail%29.png"),
    HERBLORE("https://oldschool.runescape.wiki/images/Herblore_icon_%28detail%29.png"),
    AGILITY("https://oldschool.runescape.wiki/images/Agility_icon_%28detail%29.png"),
    THIEVING("https://oldschool.runescape.wiki/images/Thieving_icon_%28detail%29.png"),
    SLAYER("https://oldschool.runescape.wiki/images/Slayer_icon_%28detail%29.png"),
    FARMING("https://oldschool.runescape.wiki/images/Farming_icon_%28detail%29.png"),
    RUNECRAFT("https://oldschool.runescape.wiki/images/Runecraft_icon_%28detail%29.png"),
    HUNTER("https://oldschool.runescape.wiki/images/Hunter_icon_%28detail%29.png"),
    CONSTRUCTION("https://oldschool.runescape.wiki/images/Construction_icon_%28detail%29.png"),
    /**
     * The level of all skills added together.
     */
    OVERALL("https://oldschool.runescape.wiki/images/Stats_icon.png");

    private final String thumbnailUrl;

    SkillThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }
}
