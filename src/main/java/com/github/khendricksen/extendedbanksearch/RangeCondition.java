package com.github.khendricksen.extendedbanksearch;

import java.util.function.ToIntFunction;
import net.runelite.client.game.ItemEquipmentStats;

/**
 * Matches items whose equipment bonus falls within an inclusive range, e.g. {@code prayer 2-6}.
 * The bounds are normalised so {@code 6-2} behaves the same as {@code 2-6}.
 */
final class RangeCondition implements ItemCondition
{
	private final ToIntFunction<ItemEquipmentStats> getter;
	private final int low;
	private final int high;

	RangeCondition(ToIntFunction<ItemEquipmentStats> getter, int a, int b)
	{
		this.getter = getter;
		this.low = Math.min(a, b);
		this.high = Math.max(a, b);
	}

	@Override
	public boolean test(ItemEquipmentStats eq)
	{
		int value = getter.applyAsInt(eq);
		return value >= low && value <= high;
	}
}
