package com.github.khendricksen.extendedbanksearch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;
import net.runelite.client.game.ItemEquipmentStats;

/**
 * Maps user-typed stat words to the equipment-bonus getter they query.
 *
 * <p>Aliases may be one or two words ({@link #MAX_WORDS}); the parser tries the longest match first,
 * so multi-word forms win over bare ones. That ordering is what disambiguates the genuinely
 * ambiguous words:
 *
 * <ul>
 *   <li>{@code magic attack} &rarr; attack-magic, {@code magic def} &rarr; defence-magic,
 *       {@code magic damage} &rarr; magic-damage%, but bare {@code magic} defaults to
 *       <b>attack</b>-magic. {@code mage} is a synonym of {@code magic} everywhere.</li>
 *   <li>{@code ranged strength} &rarr; ranged strength, {@code ranged attack} &rarr; attack-ranged,
 *       {@code ranged def} &rarr; defence-ranged, but bare {@code range}/{@code ranged} defaults to
 *       <b>attack</b>-ranged ({@code range} and {@code ranged} are interchangeable).</li>
 *   <li>Bare {@code slash}/{@code crush}/{@code stab} default to the attack bonus (how players talk).</li>
 * </ul>
 *
 * <p>Defaulting bare magic/range to attack is the riskier of these choices — unlike stab/slash/crush,
 * which only ever mean the attack bonus colloquially, magic and range genuinely have other things a player
 * might mean (defence, damage, strength).
 */
final class StatAliases
{
	/** Largest number of words in any alias; the parser joins at most this many tokens when matching. */
	static final int MAX_WORDS = 2;

	private static final Map<String, ToIntFunction<ItemEquipmentStats>> ALIASES = build();

	private static Map<String, ToIntFunction<ItemEquipmentStats>> build()
	{
		Map<String, ToIntFunction<ItemEquipmentStats>> m = new HashMap<>();

		put(m, ItemEquipmentStats::getPrayer, "prayer", "pray");
		put(m, ItemEquipmentStats::getStr, "str", "strength", "melee str", "melee strength");
		put(m, ItemEquipmentStats::getRstr,
			"rstr", "ranged str", "ranged strength", "range str", "range strength");
		// Magic damage is stored as a float percentage; truncate to int for comparison.
		put(m, eq -> (int) eq.getMdmg(), "mdmg", "magic damage", "magic dmg", "mage damage", "mage dmg");

		// Attack bonuses. Both word orders accepted.
		put(m, ItemEquipmentStats::getAstab, "astab", "stab", "stab attack", "attack stab");
		put(m, ItemEquipmentStats::getAslash, "aslash", "slash", "slash attack", "attack slash");
		put(m, ItemEquipmentStats::getAcrush, "acrush", "crush", "crush attack", "attack crush");
		put(m, ItemEquipmentStats::getAmagic,
			"amagic", "magic", "mage", "magic attack", "attack magic", "mage attack", "attack mage");
		put(m, ItemEquipmentStats::getArange,
			"arange", "range", "ranged", "ranged attack", "range attack", "attack ranged", "attack range");

		// Defence bonuses. Both word orders accepted (e.g. "magic def" and "def magic").
		put(m, ItemEquipmentStats::getDstab, "dstab", "def stab", "defence stab", "stab def", "stab defence");
		put(m, ItemEquipmentStats::getDslash,
			"dslash", "def slash", "defence slash", "slash def", "slash defence");
		put(m, ItemEquipmentStats::getDcrush,
			"dcrush", "def crush", "defence crush", "crush def", "crush defence");
		put(m, ItemEquipmentStats::getDmagic,
			"dmagic", "magic def", "magic defence", "def magic", "defence magic",
			"mage def", "mage defence", "def mage", "defence mage");
		put(m, ItemEquipmentStats::getDrange,
			"drange", "ranged def", "ranged defence", "range def", "range defence",
			"def ranged", "defence ranged", "def range", "defence range");

		put(m, ItemEquipmentStats::getAspeed, "speed", "aspeed");

		return Collections.unmodifiableMap(m);
	}

	private static void put(Map<String, ToIntFunction<ItemEquipmentStats>> m,
		ToIntFunction<ItemEquipmentStats> getter, String... aliases)
	{
		for (String alias : aliases)
		{
			m.put(alias, getter);
		}
	}

	/**
	 * Resolves a (possibly multi-word, space-joined) lowercase key to its bonus getter, or
	 * {@code null} if it isn't a known stat.
	 */
	static ToIntFunction<ItemEquipmentStats> resolve(String key)
	{
		return ALIASES.get(key);
	}

	private StatAliases()
	{
	}
}
