package universalDiscord.message;

public interface BeforeDiscordMessageSend {
    void call(DiscordMessageBody discordMessageBody);
}
