package com.github.khendricksen.extendedbanksearch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.client.game.ItemEquipmentStats;

/**
 * Parses bank-search query strings; see {@link #parse}.
 */
final class QueryParser
{
	/** A range value like {@code 2-6}: two integers joined by a single hyphen. */
	private static final Pattern RANGE = Pattern.compile("(-?\\d+)-(-?\\d+)");

	/**
	 * Parses a bank-search query string into a {@link ParsedQuery} — the conditions an item must satisfy.
	 *
	 * <p>An {@link ParsedQuery#isEmpty() empty} result means "nothing recognised", so the caller leaves
	 * the item untouched and the native item-name search handles it. Otherwise every recognised condition
	 * must be satisfied (AND) for an item to force-show.
	 *
	 * <p>Recognised:
	 * <ul>
	 *   <li>slot words — {@code helm}, {@code legs}, … (single token)</li>
	 *   <li>stat comparisons — {@code prayer>6}, {@code dstab >= 50}, {@code magic attack>20}
	 *       (stat key, possibly two words, then an operator, then an integer)</li>
	 *   <li>stat ranges — {@code prayer 2-6} (stat key then an inclusive low-high range)</li>
	 *   <li>bare stat words — {@code prayer}, {@code melee str} (a stat key with nothing comparable
	 *       after it is treated as an implicit {@code > 0})</li>
	 *   <li>warm clothing — {@code warm} / {@code warmth} / {@code wintertodt} (an item-id membership
	 *       test handled separately from the equipment conditions, see {@link WarmClothing})</li>
	 *   <li>food — {@code food} / {@code edible} / {@code eat} (matches items with an "Eat" inventory
	 *       action; also handled separately from the equipment conditions)</li>
	 * </ul>
	 *
	 * <p>Stat keys use longest-match so multi-word forms beat bare ones (see {@link StatAliases}).
	 * Unrecognised tokens are ignored — they still widen the results additively through the native name
	 * match (see README).
	 */
	static ParsedQuery parse(String query)
	{
		List<ItemCondition> conditions = new ArrayList<>();
		boolean warm = false;
		boolean food = false;
		if (query == null)
		{
			return new ParsedQuery(conditions, false, false);
		}

		// Split operators out as their own tokens so "prayer>6" and "prayer > 6" tokenise the same.
		String normalized = query.trim().toLowerCase().replaceAll("(" + Operator.PATTERN + ")", " $1 ");
		String[] tokens = normalized.split("\\s+");

		int i = 0;
		while (i < tokens.length)
		{
			if (tokens[i].isEmpty())
			{
				i++;
				continue;
			}

			if (isWarmWord(tokens[i]))
			{
				warm = true;
				i++;
				continue;
			}

			if (isFoodWord(tokens[i]))
			{
				food = true;
				i++;
				continue;
			}

			int consumed = tryStatComparison(tokens, i, conditions);
			if (consumed > 0)
			{
				i += consumed;
				continue;
			}

			EquipmentInventorySlot slot = EquipmentSlots.resolve(tokens[i]);
			if (slot != null)
			{
				conditions.add(new SlotCondition(slot));
				i++;
				continue;
			}

			// Unrecognised token -> ignore it.
			i++;
		}

		return new ParsedQuery(conditions, warm, food);
	}

	private static boolean isWarmWord(String token)
	{
		return "warm".equals(token) || "warmth".equals(token) || "wintertodt".equals(token);
	}

	private static boolean isFoodWord(String token)
	{
		return "food".equals(token) || "edible".equals(token) || "eat".equals(token);
	}

	/**
	 * Tries range, then operator+integer, then falls back to >0, once a stat key resolves.
	 * Returns the tokens consumed, or 0 if no key resolves.
	 */
	private static int tryStatComparison(String[] tokens, int start, List<ItemCondition> out)
	{
		int maxWords = Math.min(StatAliases.MAX_WORDS, tokens.length - start);
		for (int words = maxWords; words >= 1; words--)
		{
			ToIntFunction<ItemEquipmentStats> getter = StatAliases.resolve(join(tokens, start, words));
			if (getter == null)
			{
				continue;
			}

			// The longest key wins; once it resolves we never fall back to a shorter key. This is safe
			// because a multi-word key's trailing word (e.g. "def" in "stab def", "strength" in "melee
			// strength") is never an operator or number.
			int next = start + words;
			if (next < tokens.length)
			{
				// Range form: a single "low-high" token.
				int[] range = parseRange(tokens[next]);
				if (range != null)
				{
					out.add(new RangeCondition(getter, range[0], range[1]));
					return words + 1;
				}

				// Operator form: operator token then integer.
				Operator operator = Operator.fromToken(tokens[next]);
				int valueIndex = next + 1;
				if (operator != null && valueIndex < tokens.length)
				{
					Integer threshold = parseInt(tokens[valueIndex]);
					if (threshold != null)
					{
						out.add(new StatCondition(getter, operator, threshold));
						return words + 2;
					}
				}
			}

			out.add(new StatCondition(getter, Operator.GT, 0));
			return words;
		}

		return 0;
	}

	private static int[] parseRange(String token)
	{
		Matcher m = RANGE.matcher(token);
		if (!m.matches())
		{
			return null;
		}
		// parseInt over Integer.parseInt, returns null instead of throwing when a
		// bound doesn't fit in an int, so an oversized range is simply not recognised as a range
		Integer low = parseInt(m.group(1));
		Integer high = parseInt(m.group(2));
		if (low == null || high == null)
		{
			return null;
		}
		return new int[]{low, high};
	}

	private static String join(String[] tokens, int start, int count)
	{
		if (count == 1)
		{
			return tokens[start];
		}

		StringBuilder sb = new StringBuilder(tokens[start]);
		for (int i = 1; i < count; i++)
		{
			sb.append(' ').append(tokens[start + i]);
		}
		return sb.toString();
	}

	private static Integer parseInt(String token)
	{
		if (!token.matches("-?\\d+"))
		{
			return null;
		}
		try
		{
			return Integer.parseInt(token);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	private QueryParser()
	{
	}
}
