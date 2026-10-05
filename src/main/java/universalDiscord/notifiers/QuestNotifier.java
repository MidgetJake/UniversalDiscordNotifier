package universalDiscord.notifiers;

import com.google.common.collect.ImmutableList;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.message.MessageBuilder;
import universalDiscord.message.discord.Image;
import universalDiscord.notifiers.onevent.WidgetLoadHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestNotifier extends BaseNotifier implements WidgetLoadHandler {

    // Credit to: https://github.com/oliverpatrick/Enhanced-Discord-Notifications/blob/master/src/main/java/com/enhanceddiscordnotifications/EnhancedDiscordNotificationsPlugin.java
    // This method existed and seemed fairly solid.
    private static final Pattern QUEST_PATTERN_1 = Pattern.compile(".+?ve\\.*? (?<verb>been|rebuilt|.+?ed)? ?(?:the )?'?(?<quest>.+?)'?(?: [Qq]uest)?[!.]?$");
    private static final Pattern QUEST_PATTERN_2 = Pattern.compile("'?(?<quest>.+?)'?(?: [Qq]uest)? (?<verb>[a-z]\\w+?ed)?(?: f.*?)?[!.]?$");
    private static final ImmutableList<String> RFD_TAGS = ImmutableList.of("Another Cook", "freed", "defeated", "saved");
    private static final ImmutableList<String> WORD_QUEST_IN_NAME_TAGS = ImmutableList.of("Another Cook", "Doric", "Heroes", "Legends", "Observatory", "Olaf", "Waterfall");

    private String lastQuestText;


    public QuestNotifier(UniversalDiscordPlugin plugin) {
        super(plugin);
    }

    public void handleNotify() {
        String notifyMessage = Utils.replaceCommonPlaceholders(plugin.config.questNotifyMessage())
                .replaceAll("%QUEST%", Utils.asMarkdownWikiUrl(parseQuestWidget(lastQuestText)));

        MessageBuilder messageBuilder = MessageBuilder.textAsEmbed(notifyMessage, plugin.config.questSendImage());
        messageBuilder.setFirstThumbnail(new Image("https://oldschool.runescape.wiki/images/Quests.png"));
        plugin.messageHandler.sendMessage(messageBuilder);

        reset();
    }

    @Override
    public void handleWidgetLoaded(WidgetLoaded widgetLoaded) {
        if (widgetLoaded.getGroupId() == WidgetID.QUEST_COMPLETED_GROUP_ID) {
            if (plugin.config.notifyQuest()) {
                Widget quest = plugin.client.getWidget(WidgetInfo.QUEST_COMPLETED_NAME_TEXT);

                if (quest != null) {
                    lastQuestText = quest.getText();
                }
            }
        }
    }

    private String parseQuestWidget(final String text) {
        // "You have completed The Corsair Curse!"
        final Matcher questMatch1 = QUEST_PATTERN_1.matcher(text);
        // "'One Small Favour' completed!"
        final Matcher questMatch2 = QUEST_PATTERN_2.matcher(text);
        final Matcher questMatchFinal = questMatch1.matches() ? questMatch1 : questMatch2;
        if (!questMatchFinal.matches()) {
            return "Unable to find quest name!";
        }

        String quest = questMatchFinal.group("quest");
        String verb = questMatchFinal.group("verb") != null ? questMatchFinal.group("verb") : "";

        if (verb.contains("kind of")) {
            quest += " partial completion";
        } else if (verb.contains("completely")) {
            quest += " II";
        }

        if (RFD_TAGS.stream().anyMatch((quest + verb)::contains)) {
            quest = "Recipe for Disaster - " + quest;
        }

        if (WORD_QUEST_IN_NAME_TAGS.stream().anyMatch(quest::contains)) {
            quest += " Quest";
        }

        return quest;
    }

    @Override
    public boolean shouldNotify() {
        return isEnabled() && lastQuestText != null;
    }

    @Override
    public boolean isEnabled() {
        return plugin.config.notifyQuest() && !Utils.isQuestSpeedRunningWorld();
    }

    private void reset() {
        lastQuestText = null;
    }
}
