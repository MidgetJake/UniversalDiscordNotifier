package universalDiscord;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import universalDiscord.enums.DeathThumbnail;
import universalDiscord.enums.PlayerUrlService;

@ConfigGroup("universalDiscord")
public interface UniversalDiscordConfig extends Config {
    //region Discord
    @ConfigItem(
            keyName = "discordWebhook",
            name = "Discord Webhook",
            description = "The Webhook URL for the discord channel",
            position = 1
    )
    default String discordWebhook() {
        return "";
    }

    @ConfigItem(
            keyName = "playerUrl",
            name = "Player url",
            description = "The url/service used for looking up your account",
            position = 2
    )
    default PlayerUrlService playerUrlService() {
        return PlayerUrlService.OSRS_HISCORE;
    }
    //endregion

    //region Collection Log Section
    @ConfigSection(
            name = "Collection Log",
            description = "Settings for notifying about collection log",
            position = 3
    )
    String collectionSection = "Collection Log";

    @ConfigItem(
            keyName = "collectionLogEnabled",
            name = "Enable collection log",
            description = "Enable notifications for collection log",
            position = 1,
            section = collectionSection
    )
    default boolean notifyCollectionLog() {
        return false;
    }

    @ConfigItem(
            keyName = "collectionSendImage",
            name = "Send Image",
            description = "Send image with the notification",
            position = 2,
            section = collectionSection
    )
    default boolean collectionSendImage() {
        return true;
    }

    @ConfigItem(
            keyName = "collectionNotifMessage",
            name = "Notification Message",
            description = "The message to be sent through the webhook. Use %USERNAME% to insert your username and %ITEM% for the item",
            position = 3,
            section = collectionSection
    )
    default String collectionNotifyMessage() {
        return "%USERNAME% has added %ITEM% to their collection";
    }
    //endregion

    //region Pets Section
    @ConfigSection(
            name = "Pet",
            description = "Settings for notifying when obtaining a pet",
            position = 4
    )
    String petSection = "Pet";

    @ConfigItem(
            keyName = "petEnabled",
            name = "Enable pets",
            description = "Enable notifications for obtaining pets",
            position = 1,
            section = petSection
    )
    default boolean notifyPet() {
        return false;
    }

    @ConfigItem(
            keyName = "petSendImage",
            name = "Send Image",
            description = "Send image with the notification",
            position = 2,
            section = petSection
    )
    default boolean petSendImage() {
        return true;
    }

    @ConfigItem(
            keyName = "petNotifMessage",
            name = "Notification Message",
            description = "The message to be sent through the webhook. Use %USERNAME% to insert your username",
            position = 3,
            section = petSection
    )
    default String petNotifyMessage() {
        return "%USERNAME% has a funny feeling they are being followed";
    }
    //endregion

    //region Levels Section
    @ConfigSection(
            name = "Levels",
            description = "Settings for notifying when levelling a skill",
            position = 5
    )
    String levelSection = "Levels";

    @ConfigItem(
            keyName = "levelEnabled",
            name = "Enable level",
            description = "Enable notifications for gaining levels",
            position = 1,
            section = levelSection
    )
    default boolean notifyLevel() {
        return false;
    }

    @ConfigItem(
            keyName = "levelSendImage",
            name = "Send Image",
            description = "Send image with the notification",
            position = 2,
            section = levelSection
    )
    default boolean levelSendImage() {
        return true;
    }

    @ConfigItem(
            keyName = "levelInterval",
            name = "Notify Interval",
            description = "Interval between when a notification should be sent",
            position = 3,
            section = levelSection
    )
    default int levelInterval() {
        return 1;
    }

    @ConfigItem(
            keyName = "levelNotifMessage",
            name = "Notification Message",
            description = "The message to be sent through the webhook. Use %USERNAME% to insert your username and %SKILL% to insert the levelled skill(s)",
            position = 4,
            section = levelSection
    )
    default String levelNotifyMessage() {
        return "%USERNAME% has levelled %SKILL%";
    }
    //endregion

    //region Loot Section
    @ConfigSection(
            name = "Loot",
            description = "Settings for notifying when loot is dropped",
            position = 6
    )
    String lootSection = "Loot";

    @ConfigItem(
            keyName = "lootEnabled",
            name = "Enable loot",
            description = "Enable notifications for gaining loot",
            position = 1,
            section = lootSection
    )
    default boolean notifyLoot() {
        return false;
    }

    @ConfigItem(
            keyName = "lootSendImage",
            name = "Send Image",
            description = "Send image with the notification",
            position = 2,
            section = lootSection
    )
    default boolean lootSendImage() {
        return true;
    }

    @ConfigItem(
            keyName = "lootIcons",
            name = "Show loot icons",
            description = "Show icons for the loot obtained",
            position = 3,
            section = lootSection
    )
    default boolean lootIcons() {
        return false;
    }

    @ConfigItem(
            keyName = "minLootValue",
            name = "Min Loot value",
            description = "Minimum value of the loot to notify",
            position = 4,
            section = lootSection
    )
    default int minLootValue() {
        return 0;
    }

    @ConfigItem(
            keyName = "lootNotifMessage",
            name = "Notification Message",
            description = "The message to be sent through the webhook. Use %USERNAME% to insert your username, %LOOT% to insert the loot and %SOURCE% to show the source of the loot",
            position = 5,
            section = lootSection
    )
    default String lootNotifyMessage() {
        return "%USERNAME% has looted: \n\n%LOOT%\nFrom: %SOURCE%";
    }
    //endregion

    //region Death Section
    @ConfigSection(
            name = "Death",
            description = "Settings for notifying when you die",
            position = 7
    )
    String deathSection = "Death";

    @ConfigItem(
            keyName = "deathEnabled",
            name = "Enable Death",
            description = "Enable notifications for when you die",
            position = 1,
            section = deathSection
    )
    default boolean notifyDeath() {
        return false;
    }

    @ConfigItem(
            keyName = "deathSendImage",
            name = "Send Image",
            description = "Send image with the notification",
            position = 2,
            section = deathSection
    )
    default boolean deathSendImage() {
        return false;
    }

    @ConfigItem(
            keyName = "deathThumbnail",
            name = "Thumbnail to use",
            description = "Send a thumbnail with the notification",
            position = 3,
            section = deathSection
    )
    default DeathThumbnail deathThumbnail() {
        return DeathThumbnail.DEATH;
    }

    @ConfigItem(
            keyName = "deathNotifMessage",
            name = "Notification Message",
            description = "The message to be sent through the webhook. Use %USERNAME% to insert your username",
            position = 4,
            section = deathSection
    )
    default String deathNotifyMessage() {
        return "%USERNAME% has died...";
    }
    //endregion

    //region Slayer Section
    @ConfigSection(
            name = "Slayer",
            description = "Settings for notifying when you complete a slayer task",
            position = 8
    )
    String slayerSection = "Slayer";

    @ConfigItem(
            keyName = "slayerEnabled",
            name = "Enable Slayer",
            description = "Enable notifications for when you complete a slayer task",
            position = 1,
            section = slayerSection
    )
    default boolean notifySlayer() {
        return false;
    }

    @ConfigItem(
            keyName = "slayerSendImage",
            name = "Send Image",
            description = "Send image with the notification",
            position = 2,
            section = slayerSection
    )
    default boolean slayerSendImage() {
        return false;
    }

    @ConfigItem(
            keyName = "slayerNotifMessage",
            name = "Notification Message",
            description = "The message to be sent through the webhook. Use %USERNAME% to insert your username, %TASK% to insert your task, %POINTS% to show how many points you obtained and %TASKCOUNT% to show how many tasks you have completed.",
            position = 3,
            section = slayerSection
    )
    default String slayerNotifyMessage() {
        return "%USERNAME% has completed a slayer task: %TASK%, getting %POINTS% points and making that %TASKCOUNT% tasks completed";
    }
    //endregion

    //region Quest Section
    @ConfigSection(
            name = "Quests",
            description = "Settings for notifying when you complete a quest",
            position = 9
    )
    String questSection = "Quests";

    @ConfigItem(
            keyName = "questEnabled",
            name = "Enable Quest",
            description = "Enable notifications for when you complete a quest",
            position = 1,
            section = questSection
    )
    default boolean notifyQuest() {
        return false;
    }

    @ConfigItem(
            keyName = "questSendImage",
            name = "Send Image",
            description = "Send image with the notification",
            position = 2,
            section = questSection
    )
    default boolean questSendImage() {
        return false;
    }

    @ConfigItem(
            keyName = "questNotifMessage",
            name = "Notification Message",
            description = "The message to be sent through the webhook. Use %USERNAME% to insert your username and %QUEST% to insert the quest that you completed",
            position = 3,
            section = questSection
    )
    default String questNotifyMessage() {
        return "%USERNAME% has completed a quest: %QUEST%";
    }
    //endregion

    //region Clue Section
    @ConfigSection(
            name = "Clue Scrolls",
            description = "Settings for notifying when you complete a clue scroll",
            position = 10
    )
    String clueSection = "Clue Scrolls";

    @ConfigItem(
            keyName = "clueEnabled",
            name = "Enable Clue Scrolls",
            description = "Enable notifications for when you complete a clue scroll",
            position = 1,
            section = clueSection
    )
    default boolean notifyClue() {
        return false;
    }

    @ConfigItem(
            keyName = "clueSendImage",
            name = "Send Image",
            description = "Send image with the notification",
            position = 2,
            section = clueSection
    )
    default boolean clueSendImage() {
        return false;
    }

    @ConfigItem(
            keyName = "clueShowItems",
            name = "Show Item Icons",
            description = "Show item icons gained from the clue",
            position = 3,
            section = clueSection
    )
    default boolean clueShowItems() {
        return false;
    }

    @ConfigItem(
            keyName = "clueMinValue",
            name = "Min Value",
            description = "The minimum value of the items to be shown",
            position = 4,
            section = clueSection
    )
    default int clueMinValue() {
        return 0;
    }

    @ConfigItem(
            keyName = "clueNotifMessage",
            name = "Notification Message",
            description = "The message to be sent through the webhook. Use %USERNAME% to insert your username, %CLUE% to insert the clue type, %LOOT% to show the loot obtained and %COUNT% to insert how many of those clue types you have completed",
            position = 5,
            section = clueSection
    )
    default String clueNotifyMessage() {
        return "%USERNAME% has completed a %CLUE% clue, they have completed %COUNT%.\nThey obtained:\n\n%LOOT%";
    }
    //endregion
}
