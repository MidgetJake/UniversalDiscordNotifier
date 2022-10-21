package universalDiscord.message;

public class MessageBuilder {
    public String text;
    public boolean sendScreenImage;

    public BeforeDiscordMessageSend beforeDiscordMessageSend;

    public MessageBuilder(String text, boolean sendScreenImage) {
        this(text, sendScreenImage, null);
    }

    public MessageBuilder(String text, boolean sendScreenImage, BeforeDiscordMessageSend beforeDiscordMessageSend) {
        this.text = text;
        this.sendScreenImage = sendScreenImage;
        this.beforeDiscordMessageSend = beforeDiscordMessageSend;
    }
}

