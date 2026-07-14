package com.github.khendricksen.extendedbanksearch;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

// Not a JUnit test: launches the live RuneLite client with this plugin pre-loaded, for manual play-testing.
public class ExtendedBankSearchTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ExtendedBankSearchPlugin.class);
		RuneLite.main(args);
	}
}
