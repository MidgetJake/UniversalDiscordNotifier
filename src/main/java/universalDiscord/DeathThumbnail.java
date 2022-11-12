package universalDiscord;

public enum DeathThumbnail {
    NONE(null),
    DEATH("https://oldschool.runescape.wiki/images/Death.png"),
    GRAVE("https://oldschool.runescape.wiki/images/Grave.png"),
    BONES("https://oldschool.runescape.wiki/images/Bones_detail.png"),
    ANGEL_GRAVE("https://oldschool.runescape.wiki/images/Grave_%28Angel%29.png");

    public final String thumbnailUrl;

    DeathThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
