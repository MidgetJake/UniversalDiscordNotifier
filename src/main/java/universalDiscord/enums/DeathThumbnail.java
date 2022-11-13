package universalDiscord.enums;

@SuppressWarnings("unused")
public enum DeathThumbnail implements ThumbnailUrl {
    NONE(null),
    DEATH("https://oldschool.runescape.wiki/images/Death.png"),
    GRAVE("https://oldschool.runescape.wiki/images/Grave.png"),
    BONES("https://oldschool.runescape.wiki/images/Bones_detail.png"),
    ANGEL_GRAVE("https://oldschool.runescape.wiki/images/Grave_%28Angel%29.png");

    private final String thumbnailUrl;

    DeathThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }
}
