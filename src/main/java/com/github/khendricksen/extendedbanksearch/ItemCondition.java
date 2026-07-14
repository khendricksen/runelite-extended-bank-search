package com.github.khendricksen.extendedbanksearch;

import net.runelite.client.game.ItemEquipmentStats;

/**
 * A single recognised condition from a parsed query (e.g. "is a head-slot item", or "prayer &gt; 6").
 *
 * <p>The caller guarantees the item is equipable and has non-null equipment stats before calling
 * {@link #test}, so implementations never need to null-check.
 */
@FunctionalInterface
interface ItemCondition
{
	boolean test(ItemEquipmentStats eq);
}
