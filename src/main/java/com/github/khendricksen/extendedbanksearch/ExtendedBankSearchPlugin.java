package com.github.khendricksen.extendedbanksearch;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

/**
 * Extends the native in-game bank search box with stat-, slot- and warmth-based queries.
 *
 * <p>The plugin does NOT build any UI of its own. RuneLite's bank filter fires a {@code bankSearchFilter}
 * script callback once per item while the search prompt is open; the plugin hooks that event (exactly the
 * way core {@code BankPlugin} does for its {@code ha > 5k} value search) and force-shows items that
 * match the query by writing {@code 1} into the script's return slot.
 *
 * <p>The return is <b>additive</b>: writing {@code 1} only force-includes an item, and the game
 * still runs its own item-name match independently. So pure stat/slot queries are owned entirely by
 * the plugin's logic (clean AND), but mixing a name with stats (e.g. {@code dragon prayer>6}) yields an OR,
 * not an AND. See README.
 */
@Slf4j
@PluginDescriptor(
	name = "Extended Bank Search",
	description = "Search your bank by equipment stats, slot and warmth, straight from the native search box",
	tags = {"bank", "search", "stat", "stats", "slot", "equipment", "gear", "armour", "armor", "warm", "wintertodt", "food"}
)
public class ExtendedBankSearchPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ExtendedBankSearchConfig config;

	@Override
	protected void startUp()
	{
		log.debug("Extended Bank Search started");
	}

	@Override
	protected void shutDown()
	{
		log.debug("Extended Bank Search stopped");
	}

	@Subscribe
	public void onScriptCallbackEvent(ScriptCallbackEvent event)
	{
		if (!"bankSearchFilter".equals(event.getEventName()))
		{
			return;
		}

		final int[] intStack = client.getIntStack();
		final Object[] objectStack = client.getObjectStack();
		final int intStackSize = client.getIntStackSize();
		final int objectStackSize = client.getObjectStackSize();

		final int itemId = intStack[intStackSize - 1];
		final String search = (String) objectStack[objectStackSize - 1];

		if (config.debugLogging())
		{
			log.debug("bankSearchFilter: itemId={} search=\"{}\"", itemId, search);
		}

		if (matches(itemId, search))
		{
			// return true -> force-show this item (additive, ORs with the native name match)
			intStack[intStackSize - 2] = 1;
		}
		// else: leave the stack untouched -> falls through to native behaviour
	}

	/**
	 * Decide whether the query force-shows this item.
	 *
	 * <p>Parse the query; if it recognises nothing, do nothing and let the native name search handle
	 * it. Otherwise every recognised part must pass (AND):
	 * <ul>
	 *   <li>{@code warm} / {@code food} are item-property tests, evaluated independently of equipment
	 *       stats (edible items aren't equipable and warm items might not have any surfaceable stats);</li>
	 *   <li>slot / stat / range conditions require the item to be equipable with non-null equipment
	 *       stats.</li>
	 * </ul>
	 */
	private boolean matches(int itemId, String search)
	{
		ParsedQuery query = QueryParser.parse(search);
		if (query.isEmpty())
		{
			return false;
		}

		// Canonicalise once: bank items can be placeholders, and the plugin needs the base id for
		// every lookup below.
		final int canonicalId = itemManager.canonicalize(itemId);

		if (query.requiresWarm() && !WarmClothing.isWarm(canonicalId))
		{
			return false;
		}

		if (query.requiresFood() && !isFood(canonicalId))
		{
			return false;
		}

		if (!query.equipmentConditions().isEmpty())
		{
			ItemStats stats = itemManager.getItemStats(canonicalId);
			if (stats == null || !stats.isEquipable())
			{
				return false;
			}

			ItemEquipmentStats eq = stats.getEquipment();
			if (eq == null)
			{
				return false;
			}

			for (ItemCondition condition : query.equipmentConditions())
			{
				if (!condition.test(eq))
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Whether the item is edible, i.e. exposes an {@code "Eat"} inventory action. This catches food without
	 * a hardcoded list; drinks (potions, beer) use {@code "Drink"} and are deliberately not included.
	 */
	private boolean isFood(int canonicalItemId)
	{
		ItemComposition comp = itemManager.getItemComposition(canonicalItemId);
		for (String action : comp.getInventoryActions())
		{
			if ("Eat".equalsIgnoreCase(action))
			{
				return true;
			}
		}
		return false;
	}

	@Provides
	ExtendedBankSearchConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExtendedBankSearchConfig.class);
	}
}
