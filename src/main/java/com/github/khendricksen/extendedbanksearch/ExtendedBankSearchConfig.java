package com.github.khendricksen.extendedbanksearch;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(ExtendedBankSearchConfig.GROUP)
public interface ExtendedBankSearchConfig extends Config
{
	String GROUP = "extendedbanksearch";

	@ConfigItem(
		keyName = "debugLogging",
		name = "Debug logging",
		description = "Log each bank search query and tested item id to the client log. "
			+ "Useful while developing; leave off for normal play."
	)
	default boolean debugLogging()
	{
		return false;
	}
}
