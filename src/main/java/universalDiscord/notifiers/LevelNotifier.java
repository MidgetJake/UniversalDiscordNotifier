package universalDiscord.notifiers;

import lombok.extern.slf4j.Slf4j;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.enums.SkillThumbnail;
import universalDiscord.message.MessageBuilder;
import universalDiscord.message.discord.Image;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Hashtable;

@Slf4j
public class LevelNotifier extends BaseNotifier {

    private final ArrayList<String> levelledSkills = new ArrayList<>();
    private final Hashtable<String, Integer> currentLevels = new Hashtable<>();
    private boolean sendMessage = false;
    private int ticksWaited = 0;

    @Inject
    public LevelNotifier(UniversalDiscordPlugin plugin) {
        super(plugin);
    }

    @Override
    public void handleNotify() {
        StringBuilder skillMessage = new StringBuilder();
        int index = 0;
        SkillThumbnail skillThumb = SkillThumbnail.OVERALL;

        for (String skill : levelledSkills) {
            skillThumb = SkillThumbnail.valueOf(skill.toUpperCase());
            if (index == levelledSkills.size()) {
                skillThumb = SkillThumbnail.OVERALL;
                skillMessage.append(" and ");
            } else if (index > 0) {
                skillMessage.append(", ");
            }
            skillMessage.append(String.format("%s to %s", skill, currentLevels.get(skill)));
            index++;
        }

        String skillString = skillMessage.toString();
        levelledSkills.clear();
        String fullNotification = Utils.replaceCommonPlaceholders(plugin.config.levelNotifyMessage())
                .replaceAll("%SKILL%", skillString);

        MessageBuilder messageBuilder = MessageBuilder.textAsEmbed(fullNotification, plugin.config.levelSendImage());
        SkillThumbnail finalSkillThumb = skillThumb;
        messageBuilder.setFirstThumbnail(new Image(finalSkillThumb.getThumbnailUrl()));
        plugin.messageHandler.sendMessage(messageBuilder);

        reset();
    }

    @Override
    public boolean shouldNotify() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyLevel() && !Utils.isQuestSpeedRunningWorld();
    }

    public void reset() {
        sendMessage = false;
        currentLevels.clear();
        levelledSkills.clear();
    }

    private boolean checkLevelInterval(int level) {
        return plugin.config.levelInterval() <= 1
                || level == 99
                || level % plugin.config.levelInterval() == 0;
    }

    public void onTick() {
        if (!sendMessage) {
            return;
        }

        ticksWaited++;
        // We wait a couple extra ticks, so we can ensure that we process all the levels of the previous tick
        if (ticksWaited > 2) {
            ticksWaited = 0;
            handleNotify();
        }
    }

    public void handleLevelUp(String skill, int level) {
        if (isEnabled() && checkLevelInterval(level) && currentLevels.get(skill) != null) {
            if (level == currentLevels.get(skill)) {
                return;
            }
            levelledSkills.add(skill);
            sendMessage = true;
        }
        currentLevels.put(skill, level);
    }
}
