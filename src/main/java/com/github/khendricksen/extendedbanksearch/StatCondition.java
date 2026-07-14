package com.github.khendricksen.extendedbanksearch;

import java.util.function.ToIntFunction;
import net.runelite.client.game.ItemEquipmentStats;

/**
 * Matches items whose equipment bonus satisfies a comparison, e.g. {@code prayer>6} or
 * {@code dstab>=50}. The getter pulls the relevant bonus off the item's stats; the operator compares
 * it against the typed threshold.
 */
final class StatCondition implements ItemCondition
{
	private final ToIntFunction<ItemEquipmentStats> getter;
	private final Operator operator;
	private final int threshold;

	StatCondition(ToIntFunction<ItemEquipmentStats> getter, Operator operator, int threshold)
	{
		this.getter = getter;
		this.operator = operator;
		this.threshold = threshold;
	}

	@Override
	public boolean test(ItemEquipmentStats eq)
	{
		return operator.test(getter.applyAsInt(eq), threshold);
	}
}
