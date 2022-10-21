package universalDiscord;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class UniversalDiscordPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(UniversalDiscordPlugin.class);
		RuneLite.main(args);
	}
}