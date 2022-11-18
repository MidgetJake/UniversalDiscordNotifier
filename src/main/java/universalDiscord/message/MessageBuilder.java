package universalDiscord.message;

import lombok.NonNull;
import universalDiscord.Utils;
import universalDiscord.message.discord.Author;
import universalDiscord.message.discord.Embed;
import universalDiscord.message.discord.Image;
import universalDiscord.message.discord.WebhookBody;

public class MessageBuilder {
    public WebhookBody webhookBody;
    public boolean sendScreenImage;

    public static @NonNull MessageBuilder textAsEmbed(String text, boolean sendScreenImage) {
        final WebhookBody body = new WebhookBody();
        final Embed embed = Embed.builder()
                .description(text)
                .build();

        body.getEmbeds().add(embed);

        return new MessageBuilder(body, sendScreenImage);
    }

    public MessageBuilder(WebhookBody webhookBody, boolean sendScreenImage) {
        this.webhookBody = webhookBody;
        this.sendScreenImage = sendScreenImage;
    }

    public void setFirstThumbnail(Image image) {
        webhookBody.getEmbeds().stream().findFirst().ifPresent((embed -> embed.setThumbnail(image)));
    }

    public void setPlayerAsAuthorForEmbeds() {
        Author author = playerAsAuthor();
        for (Embed embed : webhookBody.getEmbeds()) {
            embed.setAuthor(author);
        }
    }

    public Author playerAsAuthor() {
        return Author.builder()
                .name(Utils.getPlayerName())
                .icon_url(Utils.getPlayerBadgeUrl())
                .url(Utils.getPlayerUrl())
                .build();
    }


}
