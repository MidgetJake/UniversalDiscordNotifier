package universalDiscord.notifiers;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.message.MessageBuilder;
import universalDiscord.notifiers.onevent.ChatMessageHandler;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlayerNotifier extends BaseNotifier implements ChatMessageHandler {
    private static final Pattern SLAYER_TASK_REGEX = Pattern.compile("You have completed your task! You killed (?<task>[\\d,]+ [\\w,]+)\\..*");
    private static final Pattern SLAYER_COMPLETE_REGEX = Pattern.compile("You've completed (?:at least )?(?<taskCount>[\\d,]+) (?:Wilderness )?tasks?(?: and received \\d+ points, giving you a total of (?<points>[\\d,]+)|\\.You'll be eligible to earn reward points if you complete tasks from a more advanced Slayer Master\\.| and reached the maximum amount of Slayer points \\((?<points2>[\\d,]+)\\))?");

    private Matcher lastTaskMatcher;
    private Matcher lastPointMatcher;

    public String slayerTask;
    public String slayerPoints;
    public String slayerTasksCompleted;

    @Inject
    public SlayerNotifier(UniversalDiscordPlugin plugin) {
        super(plugin);
    }

    @Override
    public void handleNotify() {
        String notifyMessage = Utils.replaceCommonPlaceholders(plugin.config.slayerNotifyMessage())
                .replaceAll("%TASK%", Utils.asMarkdownWikiUrl(slayerTask))
                .replaceAll("%TASKCOUNT%", slayerTasksCompleted)
                .replaceAll("%POINTS%", slayerPoints);

        MessageBuilder messageBuilder = MessageBuilder.textAsEmbed(notifyMessage, plugin.config.slayerSendImage());

        plugin.messageHandler.sendMessage(messageBuilder);

        reset();
    }

    @Override
    public boolean shouldNotify() {
        return isEnabled()
                && lastTaskMatcher != null
                && lastPointMatcher != null;
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifySlayer();
    }

    public void handleChatMessage(ChatMessage chatMessage) {
        String cleanedMessage = Text.removeTags(chatMessage.getMessage());
        if (cleanedMessage.toLowerCase().contains("slayer master")
                || cleanedMessage.contains("completed your task!")) {
            Matcher taskMatcher = SLAYER_TASK_REGEX.matcher(cleanedMessage);
            Matcher pointsMatcher = SLAYER_COMPLETE_REGEX.matcher(cleanedMessage);

            if (taskMatcher.find()) {
                lastTaskMatcher = taskMatcher;
                slayerTask = taskMatcher.group("task");
            }
            if (pointsMatcher.find()) {
                lastPointMatcher = pointsMatcher;
                slayerTasksCompleted = lastPointMatcher.group("taskCount");
                slayerPoints = lastPointMatcher.group("points");
                if (slayerPoints == null) {
                    slayerPoints = lastPointMatcher.group("points2");
                }

                // 3 different cases of seeing points, so in our worst case it's 0
                if (slayerPoints == null) {
                    slayerPoints = "0";
                }
            }
        }
    }

    private void reset() {
        slayerTask = null;
        slayerPoints = null;
        slayerTasksCompleted = null;

        lastPointMatcher = null;
        lastTaskMatcher = null;
    }
}
